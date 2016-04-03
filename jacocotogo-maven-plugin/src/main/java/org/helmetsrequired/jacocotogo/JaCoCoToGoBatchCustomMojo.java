/**
 * Copyright (C) 2013 Matthew C. Jenkins (matt@helmetsrequired.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helmetsrequired.jacocotogo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Retrieves JaCoCo execution data from multiple sources, and optionally,
 * merge the results.
 * 
 * @author Matthew C. Jenkins
 * @since 1.1
 */
@Mojo(name = "batch-custom")
public class JaCoCoToGoBatchCustomMojo extends JaCoCoToGoBatchMojo {

    /**
     * a {@link java.util.List} of {@link org.helmetsrequired.jacocotogo.Source}
     * from which JaCoCo execution data should be fetched.
     */
    @Parameter()
    private String hosts;
    
    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        List<Source> sources = new ArrayList<Source>();
        
        StringTokenizer tokenizer = new StringTokenizer(hosts, ",");
        
        String mergeFilePath = getMergeFile().getPath();
        
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            
            StringTokenizer tokenizer2 = new StringTokenizer(token, ":");
            String hostName = tokenizer2.nextToken();
            Integer port = Integer.parseInt(tokenizer2.nextToken());
            String pathName = mergeFilePath + "_" + hostName + "_" + port + "_" + i++;
            
            File outputFile = new File(pathName);
            
            Source source = new Source();
            source.setType("tcp");
            source.setHostname(hostName);
            source.setPort(port);
            source.setOutputFile(outputFile);
            
            sources.add(source);
        }
        setSources(sources);
        
        super.execute();
    }
}
