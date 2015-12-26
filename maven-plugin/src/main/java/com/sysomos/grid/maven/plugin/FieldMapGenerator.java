package com.sysomos.grid.maven.plugin;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

/**
 * Created by kkim on 12/25/15.
 */

@Mojo(name = "generate", requiresDirectInvocation = true)
@Execute(phase = LifecyclePhase.GENERATE_SOURCES)

public class FieldMapGenerator extends AbstractMojo {
    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(property = "maps", required = true)
    private List<String> maps;

    @Parameter(property = "sourceDirectory",
            required = false,
            defaultValue = "${project.basedir}/src/main/resources")
    private File sourceDirectory;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/src/main/java")
    private File outputDirectory;


    protected Set<String> getMembers(Map<String,Map<String,String>> maps) {
        Set<String> ret = new HashSet<String>();
        for(Map<String,String> map:maps.values()) {
            ret.addAll(map.keySet());
        }
        return ret;
    }

    protected String newInstance(String field,Set<String> keys,Map<String,String> value) {
        ArrayList<String> argList = new ArrayList<String>();
        for(String key:keys) {
            argList.add(value.containsKey(key)?value.get(key):"");
        }
        String args = Joiner.on(",").join(argList);
        return String.format("%s(\"%s\")",field,args);
    }

    protected String getInstance(Set<String> memberSet,Map<String, Map<String,String>> fields) {
        List<String> valueList = new ArrayList<String>();
        for (Map.Entry<String, Map<String,String>> keyValue : fields.entrySet()) {
            valueList.add(newInstance(keyValue.getKey(),memberSet,keyValue.getValue()));
        }

        return Joiner.on(",").join(valueList);
    }

    protected String getConstructor(String name,Set<String>members) {
        StringBuffer sb = new StringBuffer();
        ArrayList<String> argList = new ArrayList<String>();
        for(String arg : members) {
            argList.add(String.format("String %s",arg));
        }
        String args = Joiner.on(",").join(argList);
        sb.append(String.format("%s(%s) {",name,args));
        for(String arg : members) {
            sb.append(String.format("this.%s=%s;",arg,arg));
        }
        sb.append("}");
        return sb.toString();
    }

    protected String getMember(Set<String> members) {
        ArrayList<String> memberList = new ArrayList<String>();
        for(String member:members) {
            memberList.add(String.format("private String %s;",member));
        }
        return Joiner.on("\n").join(memberList);
    }

    protected String getFirstUpperCase(String word) {
        char [] chararray = word.toCharArray();
        chararray[0] = Character.toUpperCase(chararray[0]);
        return new String(chararray);
    }

    protected String getGetter(Set<String> members) {
        ArrayList<String> memberList = new ArrayList<String>();
        for(String member:members) {
            memberList.add(String.format("String get%s() { return %s; }",
                    getFirstUpperCase(member),
                    member));
        }

        return Joiner.on("\n").join(memberList);
    }

    protected void generate(FileWriter writer, String classPath, Map<String, Map<String,String>> fields) throws IOException {
        String[] classArray = classPath.split("\\.");
        if(classArray.length>1) {
            String packagePath = Joiner.on(".").join(
                    Arrays.copyOfRange(classArray, 0, classArray.length - 1));
                writer.write(String.format("package  %s;\n", packagePath));
        }

        if(classArray.length==0)
            return;

        String className = classArray[classArray.length - 1];
        writer.write(String.format("public enum %s  {\n",className));

        Set<String> memberSet = getMembers(fields);

        if (fields.size() > 0) {
            writer.write(String.format("%s;\n",getInstance(memberSet,fields)));
            writer.write(String.format("%s\n",getMember(memberSet)));
            writer.write(String.format("%s\n",getConstructor(className,memberSet)));
            writer.write(String.format("%s\n",getGetter(memberSet)));
        }

        writer.write("}");
        writer.flush();
        writer.close();
    }

    public void execute() throws MojoExecutionException, MojoFailureException {

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        for (String map : maps) {
            File mapfile = new File(sourceDirectory, map);
            if (mapfile.isFile()) {
                try {
                    Yaml yaml = new Yaml();

                    Map<String, Object> definition = (Map<String, Object>) yaml.load(new FileInputStream(mapfile));

                    if (definition.containsKey("class") &&
                            definition.containsKey("fields")) {
                        String fullPathClass = (String) definition.get("class");
                        File classPath = new File(outputDirectory, String.format("%s.java",fullPathClass.replace(".", "/")));
                        if (!classPath.getParentFile().exists()) {
                            classPath.getParentFile().mkdirs();
                        }

                        project.addCompileSourceRoot(outputDirectory.getPath());
                        FileWriter writer = new FileWriter(classPath);
                        generate(writer, fullPathClass, (Map<String, Map<String,String>>) definition.get("fields"));
                    }

                } catch (FileNotFoundException e) {
                    getLog().error(e);
                } catch (IOException e) {
                    getLog().error(e);
                }
            }
        }
    }
}
