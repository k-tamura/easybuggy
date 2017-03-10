package org.t246osslab.easybuggy.core.dao;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.directory.server.constants.ServerDNConstants;
import org.apache.directory.server.core.DefaultDirectoryService;
import org.apache.directory.server.core.DirectoryService;
import org.apache.directory.server.core.entry.ServerEntry;
import org.apache.directory.server.core.partition.Partition;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.shared.ldap.name.LdapDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Embedded Apache Directory Server.
 */
public class EmbeddedADS {

    private static Logger log = LoggerFactory.getLogger(EmbeddedADS.class);

    /** The directory service */
    public static DirectoryService service;

    /**
     * Create an instance of EmbeddedADS and initialize it.
     */
    static {
        try {
            service = new DefaultDirectoryService();

            // Disable the ChangeLog system
            service.getChangeLog().setEnabled(false);
            service.setDenormalizeOpAttrsEnabled(true);

            // Add system partition
            Partition systemPartition;
            systemPartition = addPartition("system", ServerDNConstants.SYSTEM_DN);
            service.setSystemPartition(systemPartition);

            // Add root partition
            Partition t246osslabPartition = addPartition("t246osslab", "dc=t246osslab,dc=org");

            // Start up the service
            service.startup();

            // Add the root entry if it does not exist
            try {
                service.getAdminSession().lookup(t246osslabPartition.getSuffixDn());
            } catch (Exception lnnfe) {
                LdapDN dnBar = new LdapDN("dc=t246osslab,dc=org");
                ServerEntry entryBar = service.newEntry(dnBar);
                entryBar.add("objectClass", "dcObject", "organization");
                entryBar.add("o", "t246osslab");
                entryBar.add("dc", "t246osslab");
                service.getAdminSession().add(entryBar);
            }

            // Add the people entries
            LdapDN peopleDn = new LdapDN("ou=people,dc=t246osslab,dc=org");
            if (!service.getAdminSession().exists(peopleDn)) {
                ServerEntry e = service.newEntry(peopleDn);
                e.add("objectClass", "organizationalUnit");
                e.add("ou", "people");
                service.getAdminSession().add(e);
            }

            // Add sample users
            addUser("Mark", "password", RandomStringUtils.randomNumeric(10));
            addUser("David", "p@s2w0rd", RandomStringUtils.randomNumeric(10));
            addUser("Peter", "pa33word", RandomStringUtils.randomNumeric(10));
            addUser("James", "pathwood", RandomStringUtils.randomNumeric(10));
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }

    // Add a partition to the server
    private static Partition addPartition(String partitionId, String partitionDn) throws Exception {
        // Create a new partition named
        Partition partition = new JdbmPartition();
        partition.setId(partitionId);
        partition.setSuffix(partitionDn);
        service.addPartition(partition);
        return partition;
    }

    // Add a user to the server
    private static void addUser(String username, String passwd, String secretNumber) throws Exception {
        LdapDN dn = new LdapDN("uid=" + username + ",ou=people,dc=t246osslab,dc=org");
        if (!service.getAdminSession().exists(dn)) {
            ServerEntry e = service.newEntry(dn);
            e.add("objectClass", "person", "inetOrgPerson");
            e.add("uid", username);
            e.add("displayName", username);
            e.add("userPassword", passwd.getBytes());
            e.add("employeeNumber", secretNumber);
            e.add("sn", "Not use");
            e.add("cn", "Not use");
            e.add("givenName", "Not use");
            service.getAdminSession().add(e);
        }
    }
}
