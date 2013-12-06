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
package co.leantechniques.maven.h2;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static co.leantechniques.maven.h2.H2DatabaseDirectoryProvider.DB_DIRECTORY_KEY;
import static co.leantechniques.maven.h2.H2DatabaseDirectoryProvider.DEFAULT_LOCATION;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class H2DatabaseDirectoryProviderTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private H2DatabaseDirectoryProvider directoryProvider;

    @Before
    public void setUp() throws Exception {
        directoryProvider = new H2DatabaseDirectoryProvider();
        System.getProperties().remove(DB_DIRECTORY_KEY);
    }

    @Test
    public void shouldProvideTheDirectoryWhenGivenTheSystemPropertyOverrideValue() {
        File directory = temporaryFolder.newFolder("test");
        System.setProperty(DB_DIRECTORY_KEY, directory.getAbsolutePath());

        assertEquals(directory, directoryProvider.provide());
    }

    @Test
    public void shouldProvideTheDefaultDirectory() {
        assertSame(DEFAULT_LOCATION, directoryProvider.provide());
    }
}
