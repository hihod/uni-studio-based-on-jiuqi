# Uni Studio based on JiuQi

## 开发配置

1. 导入项目到 eclipse

2. 自定义 Target Platform

3. 放置 dll 到磁盘路径 `C:\Users\<User Name>\AppData\Roaming\RPA`

4. 创建 Debug/Run Configuration

   > Program arguments:
   >
   > `-os ${target.os} -ws ${target.ws} -arch ${target.arch} -nl ${target.nl} -consoleLog -data  @noDefault -Clean`
   >
   > VM arguments:
   >
   > `-Xms128m -Xmx1024m -Duser.language=zh -Dosgi.instance.area.default=@user.home/ETL-workspace`

