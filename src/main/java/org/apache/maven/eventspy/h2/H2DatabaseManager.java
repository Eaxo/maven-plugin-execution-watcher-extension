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

package org.apache.maven.eventspy.h2;

import com.googlecode.flyway.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.io.File;

public class H2DatabaseManager {
    private static final Object LOCK = new Object();
    private File dbLocation;
    private DataSource cachedDataSource;
    private Flyway flyway = new Flyway();

    public H2DatabaseManager(File dbLocation) {
        this.dbLocation = dbLocation;
        dbLocation.mkdirs();
    }

    public DataSource load() {
        synchronized (LOCK) {
            if (cachedDataSource == null) {
                cachedDataSource = JdbcConnectionPool.create("jdbc:h2:" + dbLocation.getAbsolutePath() + "/stats;AUTO_SERVER=TRUE", "", "");
                flyway.setDataSource(cachedDataSource);
                flyway.setInitOnMigrate(true);
                flyway.migrate();
            }
            return cachedDataSource;
        }
    }

    public void setDirectoryOfRepository(File directoryOfRepository) {
        this.dbLocation = directoryOfRepository;
    }
}
