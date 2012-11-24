/**
 *
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.apache.maven.eventspy;

import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PluginStatsFactoryTest {
    @Mock
    private MojoExecution mojoExecution;
    @Mock
    private ExecutionEvent event;
    @InjectMocks
    private PluginStatsFactory factory = new PluginStatsFactory();

    @Test
    public void build() {
        MavenProject mavenProject = new MavenProject();
        mavenProject.setGroupId("project-groupId");
        mavenProject.setArtifactId("project-artifactId");

        when(mojoExecution.getGroupId()).thenReturn("plugin-groupId");
        when(mojoExecution.getArtifactId()).thenReturn("plugin-artifactId");
        when(mojoExecution.getVersion()).thenReturn("plugin-version");
        when(mojoExecution.getGoal()).thenReturn("plugin-goal");
        when(mojoExecution.getExecutionId()).thenReturn("execution-id");

        when(event.getType()).thenReturn(ExecutionEvent.Type.MojoSucceeded);
        when(event.getProject()).thenReturn(mavenProject);
        when(event.getMojoExecution()).thenReturn(mojoExecution);

        PluginStats stats = factory.build(event);

        assertNotNull(stats);
        assertEquals(PluginStats.Type.SUCCEED, stats.type);
        assertEquals("execution-id", stats.executionId);
        assertNotNull(stats.timestamp);

        assertPlugin(stats);
        assertProject(stats);
    }

    private void assertProject(PluginStats stats) {
        assertNotNull(stats.project);
        assertEquals("project-groupId", stats.project.groupId);
        assertEquals("project-artifactId", stats.project.artifactId);
    }

    private void assertPlugin(PluginStats stats) {
        assertNotNull(stats.plugin);
        assertEquals("plugin-groupId", stats.plugin.groupId);
        assertEquals("plugin-artifactId", stats.plugin.artifactId);
        assertEquals("plugin-version", stats.plugin.version);
        assertEquals("plugin-goal", stats.plugin.goal);
    }
}
