import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateCode {

    private static final Logger logger = LoggerFactory.getLogger(GenerateCode.class);

    /**
     * 要生成的数据库表
     */
    private static final String[] TABLES = {"blog_article_praise"};

    /**
     * 实体类路径
     */
    private static final String PATH_PO = "cn.postwall.blog.pojo.po";
    /**
     * 数据库映射路径
     */
    private static final String PATH_MAPPER = "cn.postwall.blog.mapper";
    /**
     * 服务层映射路径
     */
    private static final String PATH_SERVICE = "cn.postwall.blog.service";

    /**
     * 数据库连接信息
     */
    private static final String URL = "jdbc:mysql:///postwall-blog?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "775957025";

    public static final String projectPath = getProjectPath();
    public static final String resourcePath = getProjectResource();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        for (String tableName : TABLES) {
            List<Map<String, String>> tableList = mysqlTableStructure(tableName);
            if (tableList == null || tableList.size() == 0) {
                return;
            }
            // 生成实体类
            generateCodeEntity(tableList, tableName);
            // 生成映射文件
            generateCodeMapper(tableList, tableName);
            // 生成服务层
            generateCodeService(tableList, tableName);
        }
        logger.info("完成生成耗时:{}ms",System.currentTimeMillis() - startTime);
    }

    /**
     * 自动生成服务层代码
     */
    private static void generateCodeService(List<Map<String, String>> columnList, String tableName) {
        StringBuilder outString = new StringBuilder();
        // 服务层接口
        String IServiceName = "I"+upperCase(underlineToCamel(tableName.replace("t_", ""))) + "Service";
        outString.append("package ").append(PATH_SERVICE).append(";").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append(generateNotes())
                .append("public interface ").append(IServiceName).append(" {").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("}");
        String servicePath = PATH_SERVICE.replace(".", "\\");
        File IServiceFile = new File(projectPath + servicePath, IServiceName + ".java");
        boolean flag = exportFile(IServiceFile, outString);
        if (flag) {
            logger.info("生成服务层接口：{}", IServiceFile.getAbsoluteFile());
        }

        // 服务层实现类
        outString.setLength(0);

        String serviceName = upperCase(underlineToCamel(tableName.replace("t_", ""))) + "ServiceImpl";
        String mapperName = upperCase(underlineToCamel(tableName.replace("t_", ""))) + "Mapper";
        outString.append("package ").append(PATH_SERVICE).append(".impl").append(";").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("import ").append(PATH_MAPPER).append(".").append(mapperName).append(";").append(System.getProperty("line.separator"))
                .append("import ").append(PATH_SERVICE).append(".").append(IServiceName).append(";").append(System.getProperty("line.separator"))
                .append("import org.springframework.beans.factory.annotation.Autowired;").append(System.getProperty("line.separator"))
                .append("import org.springframework.stereotype.Service;").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append(generateNotes())
                .append("@Service").append(System.getProperty("line.separator"))
                .append("public class ").append(serviceName).append(" implements ").append(IServiceName).append(" {").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("    @Autowired").append(System.getProperty("line.separator"))
                .append("    private ").append(mapperName).append(" ").append(lowerCase(mapperName)).append(";").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("}");

        File ServiceImplFile = new File(projectPath + servicePath + "\\impl\\" ,   serviceName + ".java");
        boolean flag2 = exportFile(ServiceImplFile, outString);
        if (flag2) {
            logger.info("生成服务层实现类：{}", ServiceImplFile.getAbsoluteFile());
        }
    }

    /**
     * 自动生成数据库映射文件
     */
    private static void generateCodeMapper(List<Map<String, String>> columnList, String tableName) {
        StringBuilder outString = new StringBuilder();
        String name = upperCase(underlineToCamel(tableName.replace("t_", "")));
        String entityName = name + "PO";
        String mapperName =  name + "Mapper";
        outString.append("package ").append(PATH_MAPPER).append(";").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("import org.apache.ibatis.annotations.Param;").append(System.getProperty("line.separator"))
                .append("import ").append(PATH_PO).append(".").append(entityName).append(";").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("import java.util.List;").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append(generateNotes())
                .append("public interface ").append(mapperName).append(" {").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("    List<").append(entityName).append("> page").append(name).append("(@Param(\"curPage\") int curPage, ")
                .append("@Param(\"pageSize\") int pageSize);").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("    int count").append(name).append("();").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("    int insert").append(name).append("(@Param(\"").append(lowerCase(name)).append("\") ").append(entityName)
                .append(" ").append(lowerCase(name)).append(");")
                .append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("    int update").append(name).append("(@Param(\"").append(lowerCase(name)).append("\") ").append(entityName)
                .append(" ").append(lowerCase(name)).append(");")
                .append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("}");


        String mapperPath = PATH_MAPPER.replace(".", "\\");
        File mapperFile = new File(projectPath + mapperPath, mapperName + ".java");
        boolean b = exportFile(mapperFile, outString);
        if (b) {
            logger.info("生成数据库接口文件：{}", mapperFile.getAbsoluteFile());
        }

        outString.setLength(0);

        //生成xml文件
        outString.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(System.getProperty("line.separator"))
                .append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"").append(System.getProperty("line.separator"))
                .append("\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">").append(System.getProperty("line.separator"))
                .append("<mapper namespace=\"").append(PATH_MAPPER).append(".").append(mapperName).append("\">").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("    <resultMap id=\"basePOMap\" type=\"").append(PATH_PO).append(".").append(entityName).append("\">").append(System.getProperty("line.separator"));

        String columns = "";
        // 插入
        String insertValue = "";
        // 更新
        String updateValue = "";
        String updateKey = "";
        for (Map<String, String> map : columnList) {
            String columnName = map.get("columnName");
            String indexName = map.get("indexName");

            columns += columnName + ", ";

            if ("PRIMARY".equals(indexName)) {
                outString.append("        <id column=\"").append(columnName).append("\" property=\"")
                        .append(underlineToCamel(columnName)).append("\" />").append(System.getProperty("line.separator"));
                updateKey += columnName + " = #{"+lowerCase(name)+"."+underlineToCamel(columnName)+"} ";
            } else {
                outString.append("        <result column=\"").append(columnName).append("\" property=\"")
                        .append(underlineToCamel(columnName)).append("\" />").append(System.getProperty("line.separator"));

                if (columnName.equals("create_time") || columnName.equals("update_time")) {
                    insertValue += "        now(), " + System.getProperty("line.separator");
                    if (!columnName.equals("create_time")) {
                        updateValue += "        " + columnName + " = now(), " + System.getProperty("line.separator");
                    }
                } else {
                    // 插入
                    insertValue += "        #{" + lowerCase(name) + "." + underlineToCamel(columnName)+"}, " + System.getProperty("line.separator");
                    if (!columnName.equals("create_user")) {
                        //更新
                        updateValue += "        " + columnName + " = #{"+lowerCase(name) + "." + underlineToCamel(columnName)+"}, " + System.getProperty("line.separator");
                    }
                }
            }
        }

        outString.append("    </resultMap>").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("    <sql id=\"baseColumn\">").append(System.getProperty("line.separator"))
                .append("        ")
                .append(columns, 0, columns.length() - 2).append(System.getProperty("line.separator"))
                .append("    </sql>").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("    <sql id=\"baseTable\">").append(System.getProperty("line.separator"))
                .append("        ").append(tableName).append(System.getProperty("line.separator"))
                .append("    </sql>").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("    <insert id=\"insert"+name+"\">").append(System.getProperty("line.separator"))
                .append("        INSERT INTO ").append(System.getProperty("line.separator"))
                .append("        <include refid=\"baseTable\"/>(<include refid=\"baseColumn\"/>)").append(System.getProperty("line.separator"))
                .append("        VALUES(null, ").append(System.getProperty("line.separator")).append(insertValue, 0, insertValue.length() - 4).append(")").append(System.getProperty("line.separator"))
                .append("    </insert>").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator")).append(System.getProperty("line.separator"))
                .append("    <update id=\"update"+name+"\">").append(System.getProperty("line.separator"))
                .append("        UPDATE ").append(System.getProperty("line.separator"))
                .append("        <include refid=\"baseTable\"/>").append(System.getProperty("line.separator"))
                .append("        SET ").append(System.getProperty("line.separator")).append(updateValue, 0 , updateValue.length() - 4).append(System.getProperty("line.separator"))
                .append("        WHERE ").append(updateKey).append(System.getProperty("line.separator"))
                .append("    </update>").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("    <select id=\"page"+name+"\" resultMap=\"basePOMap\">").append(System.getProperty("line.separator"))
                .append("        SELECT ").append(System.getProperty("line.separator"))
                .append("        <include refid=\"baseColumn\"/>").append(System.getProperty("line.separator"))
                .append("        FROM").append(System.getProperty("line.separator"))
                .append("        <include refid=\"baseTable\"/>").append(System.getProperty("line.separator"))
                .append("        LIMIT #{curPage}, #{pageSize}").append(System.getProperty("line.separator"))
                .append("    </select>").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("    <select id=\"count"+name+"\" resultType=\"java.lang.Integer\">").append(System.getProperty("line.separator"))
                .append("        SELECT").append(System.getProperty("line.separator"))
                .append("        count(id)").append(System.getProperty("line.separator"))
                .append("        FROM").append(System.getProperty("line.separator"))
                .append("        <include refid=\"baseTable\"/>").append(System.getProperty("line.separator"))
                .append("    </select>").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("</mapper>");

        File mapperXmlFile = new File(resourcePath , mapperName + ".xml");
        boolean flag = exportFile(mapperXmlFile, outString);
        if (flag) {
            logger.info("生成数据库XML文件：{}", mapperXmlFile.getAbsoluteFile());
        }

    }

    /**
     * 自动生成实体类代码
     */
    private static void generateCodeEntity(List<Map<String, String>> columnList, String tableName) {
        // 实体类代码
        StringBuilder outString = new StringBuilder();
        String entityName = upperCase(underlineToCamel(tableName.replace("t_", ""))) + "PO";
        outString.append("package ").append(PATH_PO).append(";").append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"))
                .append("import lombok.Data;").append(System.getProperty("line.separator"))
                .append(generateNotes())
                .append("@Data").append(System.getProperty("line.separator"))
                .append("public class ").append(entityName).append(" {").append(System.getProperty("line.separator"));
        for (Map<String, String> map : columnList) {
            outString.append(System.getProperty("line.separator"));
            String columnType = map.get("columnType");
            String columnName = map.get("columnName");
            String columnComment = map.get("columnComment");
            //添加注释
            outString.append("    /**").append(System.getProperty("line.separator"))
                    .append("     * ").append(columnComment).append(System.getProperty("line.separator"))
                    .append("     */").append(System.getProperty("line.separator"));
            if (columnType.indexOf("tinyint") != -1) {
                outString.append("    private Integer ");
            } else if (columnType.indexOf("int") != -1) {
                outString.append("    private Long ");
            } else {
                outString.append("    private String ");
            }
            outString.append(underlineToCamel(columnName)).append(";").append(System.getProperty("line.separator"));
        }
        outString.append(System.getProperty("line.separator"));
        outString.append("}");

        // 导出实体类代码
        String filePath = PATH_PO.replace(".", "\\");
        File file = new File(projectPath + filePath,entityName + ".java");
        boolean flag = exportFile(file, outString);
        if (flag) {
            logger.info("生成实体类：{}", file.getAbsoluteFile());
        }
    }

    /**
     * 导出文件
     * @param file
     * @param outString
     */
    private static boolean exportFile(File file, StringBuilder outString) {
        // 如果文件存在，直接返回
        if (file.exists()) {
            return false;
        }

        OutputStream outputStream = null;
        Writer writer = null;
        try {
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, "UTF-8");
            writer.write(outString.toString());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 生成作者信息
     * @return
     */
    private static String generateNotes() {
        StringBuilder notes = new StringBuilder();
        notes.append("/**").append(System.getProperty("line.separator"))
                .append("* @author liuhanchao").append(System.getProperty("line.separator"))
                .append("* @date ").append(nowDate()).append(System.getProperty("line.separator"))
                .append("* @Description:").append(System.getProperty("line.separator"))
                .append("*/").append(System.getProperty("line.separator"));
        return notes.toString();
    }

    /**
     * 首字母大写
     * @param str
     * @return
     */
    private static String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 首字母小写
     * @param str
     * @return
     */
    private static String lowerCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] = (char) (ch[0] + 32);
        }
        return new String(ch);
    }

    /**
     * 下划线转驼峰法
     * @param line 源字符串
     * @return 转换后的字符串
     */
    private static String underlineToCamel(String line){
        if(line==null||"".equals(line)){
            return "";
        }
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
            int index=word.lastIndexOf('_');
            if(index>0){
                sb.append(word.substring(1, index).toLowerCase());
            }else{
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }


    /**
     * 获取工程路径
     * @return
     */
    private static String getProjectPath() {
        File directory = new File("");//参数为空
        String projectFile = null;
        try {
            projectFile = directory.getCanonicalPath() + "\\blog-api\\src\\main\\java\\";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectFile;
    }

    /**
     * 获取工程路径
     * @return
     */
    private static String getProjectResource() {
        File directory = new File("");//参数为空
        String courseFile = null;
        try {
            courseFile = directory.getCanonicalPath() + "\\blog-api\\src\\main\\resources\\mapper\\";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courseFile;
    }

    /**
     *  获取数据库结构
     */
    private static List<Map<String, String>> mysqlTableStructure(String tableName) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, String>> resList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            StringBuilder sqlStr = new StringBuilder();
            sqlStr.append("SELECT a.COLUMN_NAME, a.COLUMN_TYPE, c.INDEX_NAME, a.COLUMN_COMMENT ")
                    .append("FROM INFORMATION_SCHEMA.COLUMNS a ")
                    .append("LEFT JOIN information_schema.statistics c ON a.TABLE_NAME = c.TABLE_NAME ")
                    .append("AND a.table_schema = c.table_schema AND a.COLUMN_NAME = c.COLUMN_NAME ")
                    .append("WHERE a.table_schema = ? AND a.TABLE_NAME = ?");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            ps = conn.prepareStatement(sqlStr.toString());
            ps.setString(1, getTableName(URL));
            ps.setString(2, tableName);
            rs = ps.executeQuery();
            int dbloop;
            Map<String, String> res;
            while (rs.next()) {
                dbloop = 1;
                res = new HashMap<>();
                res.put("columnName", rs.getString(dbloop++));
                res.put("columnType", rs.getString(dbloop++));
                res.put("indexName", rs.getString(dbloop++));
                res.put("columnComment", rs.getString(dbloop++));
                resList.add(res);
            }
            return resList;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 根据url获取库名
     * @param url
     * @return
     */
    private static String getTableName(String url) {
        Pattern p = Pattern.compile("jdbc:(?<db>\\w+)://(?<host>\\w+):(?<port>\\w+)/(?<dbName>([^.?]*)\\w+).*");
        Matcher m = p.matcher(url);
        if(m.find()) {
            return m.group("dbName");
        }
        p = Pattern.compile("jdbc:(?<db>\\w+):///(?<dbName>([^.?]*)\\w+).*");
        m = p.matcher(url);
        if(m.find()) {
            return m.group("dbName");
        }
        return null;
    }

    /**
     * 生成当前时间
     * @return yyyy/MM/dd HH:mm:ss
     */
    private static String nowDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(new Date());
    }

}
