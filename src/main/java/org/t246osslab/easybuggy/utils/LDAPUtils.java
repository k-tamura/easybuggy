package org.t246osslab.easybuggy.utils;


/**
 * Utility class to provide LDAP features.
 */
public class LDAPUtils {

    public static final String escapeLDAPSearchFilter(String filter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filter.length(); i++) {
            char curChar = filter.charAt(i);
            switch (curChar) {
            case '\\':
                sb.append("\\5c");
                break;
            case '*':
                sb.append("\\2a");
                break;
            case '(':
                sb.append("\\28");
                break;
            case ')':
                sb.append("\\29");
                break;
            case '\u0000':
                sb.append("\\00");
                break;
            default:
                sb.append(curChar);
            }
        }
        return sb.toString();
    }
}
