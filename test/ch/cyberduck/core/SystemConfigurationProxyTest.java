package ch.cyberduck.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @version $Id$
 */
public class SystemConfigurationProxyTest extends AbstractTestCase {

    @Test
    public void testFind() throws Exception {
        final SystemConfigurationProxy proxy = new SystemConfigurationProxy();
        assertEquals(Proxy.Type.DIRECT, proxy.find(new Host("cyberduck.io")).getType());
//        assertEquals(Proxy.Type.HTTP, proxy.find(new Host(ProtocolFactory.WEBDAV, "cyberduck.io")).getType());
//        assertEquals(Proxy.Type.HTTPS, proxy.find(new Host(ProtocolFactory.WEBDAV_SSL, "cyberduck.io")).getType());
    }

    @Test
    public void testPassiveFTP() throws Exception {
        final ProxyFinder proxy = new SystemConfigurationProxy();
        assertTrue(proxy.usePassiveFTP());
    }

    @Test
    public void testExcludedLocalHost() throws Exception {
        final SystemConfigurationProxy proxy = new SystemConfigurationProxy();
        assertEquals(Proxy.Type.DIRECT, proxy.find(new Host("cyberduck.local")).getType());
    }

    @Test
    public void testSimpleExcluded() throws Exception {
        final SystemConfigurationProxy proxy = new SystemConfigurationProxy();
        assertEquals(Proxy.Type.DIRECT, proxy.find(new Host(ProtocolFactory.WEBDAV, "simple")).getType());
    }
}
