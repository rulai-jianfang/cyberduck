package ch.cyberduck.core.cloudfront;

import ch.cyberduck.core.AbstractTestCase;
import ch.cyberduck.core.DefaultHostKeyController;
import ch.cyberduck.core.DisabledLoginController;
import ch.cyberduck.core.Host;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.cdn.Distribution;
import ch.cyberduck.core.cdn.features.Cname;
import ch.cyberduck.core.cdn.features.DistributionLogging;
import ch.cyberduck.core.cdn.features.Index;
import ch.cyberduck.core.cdn.features.Purge;
import ch.cyberduck.core.identity.IdentityConfiguration;
import ch.cyberduck.core.s3.S3Protocol;
import ch.cyberduck.core.s3.S3Session;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @version $Id$
 */
public class WebsiteCloudFrontDistributionConfigurationTest extends AbstractTestCase {

    @Test
    public void testGetMethodsAWS() throws Exception {
        final S3Session session = new S3Session(new Host(new S3Protocol()));
        final WebsiteCloudFrontDistributionConfiguration configuration = new WebsiteCloudFrontDistributionConfiguration(session);
        assertTrue(configuration.getMethods(
                new Path(new Path("/", Path.VOLUME_TYPE), "/bbb", Path.VOLUME_TYPE)).contains(Distribution.DOWNLOAD));
        assertTrue(configuration.getMethods(
                new Path(new Path("/", Path.VOLUME_TYPE), "/bbb", Path.VOLUME_TYPE)).contains(Distribution.STREAMING));
        assertFalse(configuration.getMethods(
                new Path(new Path("/", Path.VOLUME_TYPE), "/bbb", Path.VOLUME_TYPE)).contains(Distribution.CUSTOM));
        assertTrue(configuration.getMethods(
                new Path(new Path("/", Path.VOLUME_TYPE), "/bbb", Path.VOLUME_TYPE)).contains(Distribution.WEBSITE_CDN));
        assertTrue(configuration.getMethods(
                new Path(new Path("/", Path.VOLUME_TYPE), "/bbb", Path.VOLUME_TYPE)).contains(Distribution.WEBSITE));
        assertFalse(configuration.getMethods(
                new Path(new Path("/", Path.VOLUME_TYPE), "/bbb_b", Path.VOLUME_TYPE)).contains(Distribution.WEBSITE));
    }

    @Test
    public void testGetMethodsNonAWS() throws Exception {
        final S3Session session = new S3Session(new Host("custom"));
        final WebsiteCloudFrontDistributionConfiguration configuration = new WebsiteCloudFrontDistributionConfiguration(session);
        assertFalse(configuration.getMethods(
                new Path(new Path("/", Path.VOLUME_TYPE), "/bbb", Path.VOLUME_TYPE)).contains(Distribution.DOWNLOAD));
        assertFalse(configuration.getMethods(
                new Path(new Path("/", Path.VOLUME_TYPE), "/bbb", Path.VOLUME_TYPE)).contains(Distribution.STREAMING));
        assertFalse(configuration.getMethods(
                new Path(new Path("/", Path.VOLUME_TYPE), "/bbb", Path.VOLUME_TYPE)).contains(Distribution.CUSTOM));
        assertFalse(configuration.getMethods(
                new Path(new Path("/", Path.VOLUME_TYPE), "/bbb", Path.VOLUME_TYPE)).contains(Distribution.WEBSITE_CDN));
        assertTrue(configuration.getMethods(
                new Path(new Path("/", Path.VOLUME_TYPE), "/bbb", Path.VOLUME_TYPE)).contains(Distribution.WEBSITE));
        assertFalse(configuration.getMethods(
                new Path(new Path("/", Path.VOLUME_TYPE), "/bbb_b", Path.VOLUME_TYPE)).contains(Distribution.WEBSITE));
    }

    @Test
    public void testGetName() throws Exception {
        final S3Session session = new S3Session(new Host(new S3Protocol(), new S3Protocol().getDefaultHostname()));
        final CloudFrontDistributionConfiguration configuration = new WebsiteCloudFrontDistributionConfiguration(
                session
        );
        assertEquals("Amazon CloudFront", configuration.getName());
        assertEquals("Amazon CloudFront", configuration.getName(Distribution.DOWNLOAD));
        assertEquals("Amazon CloudFront", configuration.getName(Distribution.STREAMING));
        assertEquals("Amazon CloudFront", configuration.getName(Distribution.WEBSITE_CDN));
        assertEquals("Website Configuration (HTTP)", configuration.getName(Distribution.WEBSITE));
    }

    @Test
    public void testGetOrigin() throws Exception {
        final S3Session session = new S3Session(new Host(new S3Protocol(), new S3Protocol().getDefaultHostname()));
        final CloudFrontDistributionConfiguration configuration = new WebsiteCloudFrontDistributionConfiguration(
                session
        );
        assertEquals("bbb.s3.amazonaws.com",
                configuration.getOrigin(new Path("/bbb", Path.VOLUME_TYPE), Distribution.DOWNLOAD).getHost());
        assertEquals("bbb.s3.amazonaws.com",
                configuration.getOrigin(new Path("/bbb", Path.VOLUME_TYPE), Distribution.WEBSITE).getHost());
        final Path container = new Path("/bbb", Path.VOLUME_TYPE);
        container.attributes().setRegion("US");
        assertEquals("bbb.s3-website-us-east-1.amazonaws.com",
                configuration.getOrigin(container, Distribution.WEBSITE_CDN).getHost());
        container.attributes().setRegion("us-west-2");
        assertEquals("bbb.s3-website-us-west-2.amazonaws.com",
                configuration.getOrigin(container, Distribution.WEBSITE_CDN).getHost());
    }

    @Test
    public void testReadNoWebsiteConfiguration() throws Exception {
        final Host host = new Host(new S3Protocol(), new S3Protocol().getDefaultHostname());
        host.setCredentials(properties.getProperty("s3.key"), properties.getProperty("s3.secret"));
        final S3Session session = new S3Session(host);
        session.open(new DefaultHostKeyController());
        final WebsiteCloudFrontDistributionConfiguration configuration
                = new WebsiteCloudFrontDistributionConfiguration(session);
        final Path container = new Path("test.cyberduck.ch", Path.VOLUME_TYPE);
        final Distribution distribution = configuration.read(container, Distribution.WEBSITE, new DisabledLoginController());
        assertEquals("The specified bucket does not have a website configuration", distribution.getStatus());
    }

    @Test
    public void testFeatures() {
        final CloudFrontDistributionConfiguration d = new WebsiteCloudFrontDistributionConfiguration(
                new S3Session(new Host(new S3Protocol(), new S3Protocol().getDefaultHostname()))
        );
        assertNotNull(d.getFeature(Purge.class, Distribution.DOWNLOAD));
        assertNotNull(d.getFeature(Purge.class, Distribution.WEBSITE_CDN));
        assertNull(d.getFeature(Purge.class, Distribution.STREAMING));
        assertNull(d.getFeature(Purge.class, Distribution.WEBSITE));
        assertNotNull(d.getFeature(Index.class, Distribution.DOWNLOAD));
        assertNotNull(d.getFeature(Index.class, Distribution.WEBSITE_CDN));
        assertNotNull(d.getFeature(Index.class, Distribution.WEBSITE));
        assertNull(d.getFeature(Index.class, Distribution.STREAMING));
        assertNotNull(d.getFeature(DistributionLogging.class, Distribution.DOWNLOAD));
        assertNotNull(d.getFeature(Cname.class, Distribution.DOWNLOAD));
        assertNotNull(d.getFeature(Cname.class, Distribution.WEBSITE));
        assertNotNull(d.getFeature(IdentityConfiguration.class, Distribution.DOWNLOAD));
    }
}

