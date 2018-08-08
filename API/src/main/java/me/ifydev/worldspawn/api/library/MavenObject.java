package me.ifydev.worldspawn.api.library;

import lombok.Getter;

/**
 * @author Innectic
 * @since 08/08/2018
 */
public class MavenObject {
    @Getter private String groupId = null;
    @Getter private String artifactId = null;
    @Getter private String version = null;
    @Getter private String repo = "http://repo1.maven.org/maven2";

    /**
     * Instantiates a new Maven object.
     *
     * @param groupId    the group id
     * @param artifactId the artifact id
     * @param version    the version
     */
    public MavenObject(String groupId, String artifactId, String version) {
        this(groupId, artifactId, version, "http://repo1.maven.org/maven2");
    }

    /**
     * Instantiates a new Maven object.
     *
     * @param groupId    the group id
     * @param artifactId the artifact id
     * @param version    the version
     * @param repo       the repo
     */
    public MavenObject(String groupId, String artifactId, String version, String repo) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.repo = repo;
    }
}
