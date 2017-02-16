package org.t246osslab.easybuggy.servers;

import java.util.HashSet;

import org.apache.directory.server.constants.ServerDNConstants;
import org.apache.directory.server.core.DefaultDirectoryService;
import org.apache.directory.server.core.DirectoryService;
import org.apache.directory.server.core.entry.ServerEntry;
import org.apache.directory.server.core.partition.Partition;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmIndex;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.server.xdbm.Index;
import org.apache.directory.shared.ldap.name.LdapDN;

/**
 * Embedded Apache Directory Server.
 */
public class EmbeddedADS {

    /** The directory service */
    public DirectoryService service;

    /**
     * Create an instance of EmbeddedADS and initialize it.
     * 
     * @throws Exception If something went wrong
     */
    public EmbeddedADS() throws Exception {
        service = new DefaultDirectoryService();

        // Disable the ChangeLog system
        service.getChangeLog().setEnabled(false);
        service.setDenormalizeOpAttrsEnabled(true);

        // then the system partition
        // this is a MANDATORY partition
        Partition systemPartition = addPartition("system", ServerDNConstants.SYSTEM_DN);
        service.setSystemPartition(systemPartition);

        // Now we can create as many partitions as we need
        Partition t246osslabPartition = addPartition("t246osslab", "dc=t246osslab,dc=org");

        // Index some attributes on the apache partition
        addIndex(t246osslabPartition, "objectClass", "ou", "uid");

        // Start up the service
        service.startup();

        // Inject the foo root entry if it does not already exist
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

        // add the people and groups entries
        LdapDN peopleDn = new LdapDN("ou=people,dc=t246osslab,dc=org");
        if (!service.getAdminSession().exists(peopleDn)) {
            ServerEntry e = service.newEntry(peopleDn);
            e.add("objectClass", "organizationalUnit");
            e.add("ou", "people");
            service.getAdminSession().add(e);
        }

        LdapDN groupsDn = new LdapDN("ou=groups,dc=t246osslab,dc=org");
        if (!service.getAdminSession().exists(groupsDn)) {
            ServerEntry e = service.newEntry(groupsDn);
            e.add("objectClass", "organizationalUnit");
            e.add("ou", "groups");
            service.getAdminSession().add(e);
        }

        // add sample users
        addUser("Mark", "password", "57249037993");
        addUser("David", "p@s2w0rd", "42368923031");
        addUser("Peter", "pa33word", "54238496555");
        addUser("James", "pathwood", "70414823225");
    }
    
    /**
     * Add a new partition to the server
     * 
     * @param partitionId The partition Id
     * @param partitionDn The partition DN
     * @return The newly added partition
     * @throws Exception If the partition can't be added
     */
    private Partition addPartition(String partitionId, String partitionDn) throws Exception {
        // Create a new partition named 'foo'.
        Partition partition = new JdbmPartition();
        partition.setId(partitionId);
        partition.setSuffix(partitionDn);
        service.addPartition(partition);

        return partition;
    }

    /**
     * Add a new set of index on the given attributes
     * 
     * @param partition The partition on which we want to add index
     * @param attrs The list of attributes to index
     */
    private void addIndex(Partition partition, String... attrs) {
        // Index some attributes on the apache partition
        HashSet<Index<?, ServerEntry>> indexedAttributes = new HashSet<Index<?, ServerEntry>>();

        for (String attribute : attrs) {
            indexedAttributes.add(new JdbmIndex<String, ServerEntry>(attribute));
        }

        ((JdbmPartition) partition).setIndexedAttributes(indexedAttributes);
    }

    private void addUser(String username, String passwd, String secretNumber) throws Exception {
        LdapDN dn = new LdapDN("uid=" + username + ",ou=people,dc=t246osslab,dc=org");
        if (!service.getAdminSession().exists(dn)) {
            ServerEntry e = service.newEntry(dn);
            e.add("objectClass", "person", "inetOrgPerson");
            e.add("uid", username);
            e.add("givenName", username);
            e.add("sn", username);
            e.add("cn", username);
            e.add("displayName", username);
            e.add("userPassword", passwd.getBytes());
            e.add("employeeNumber", secretNumber);
            service.getAdminSession().add(e);
        }
    }

}
