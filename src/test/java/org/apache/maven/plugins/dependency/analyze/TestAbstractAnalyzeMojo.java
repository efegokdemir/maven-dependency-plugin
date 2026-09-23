/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.plugins.dependency.analyze;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestAbstractAnalyzeMojo {

    @Test
    @SuppressWarnings("unchecked")
    void filterDependenciesDoesNotMutateInput() throws Exception {
        AnalyzeMojo mojo = new AnalyzeMojo(null, new MavenProject());
        Method filterDependencies = AbstractAnalyzeMojo.class.getDeclaredMethod("filterDependencies", Set.class, String[].class);
        filterDependencies.setAccessible(true);

        Artifact ignored = artifact("org.example", "ignored");
        Artifact retained = artifact("org.example", "retained");
        Set<Artifact> artifacts = new LinkedHashSet<>();
        artifacts.add(ignored);
        artifacts.add(retained);

        Set<Artifact> result = (Set<Artifact>) filterDependencies.invoke(mojo, artifacts, new String[] {"org.example:ignored"});

        assertEquals(2, artifacts.size());
        assertTrue(artifacts.contains(ignored));
        assertTrue(artifacts.contains(retained));
        assertEquals(1, result.size());
        assertTrue(result.contains(ignored));
    }

    private Artifact artifact(String groupId, String artifactId) {
        Artifact artifact = mock(Artifact.class);
        when(artifact.getGroupId()).thenReturn(groupId);
        when(artifact.getArtifactId()).thenReturn(artifactId);
        when(artifact.getType()).thenReturn("jar");
        when(artifact.getClassifier()).thenReturn(null);
        return artifact;
    }
}
