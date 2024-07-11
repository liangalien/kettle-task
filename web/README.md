## Kettle在线任务调度平台


1、支持上传、下载ktr/kjb文件

2、支持手动执行、定时执行

3、支持日志实时查看

后续功能：项目管理、用户管理...



### 开发与编译
#### 后端springboot

##### 执行建表脚本
./script/db.sql

##### 修改配置文件application.yml

```
datasource:
    url: jdbc:mysql://127.0.0.1:3306/kt?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false
    username: root
    password: 123456
    

kettle:
  repo: ${user.dir}/repo  # 上传的文件放到哪个目录
  plugins: ${user.dir}/plugins  # kettle第三方插件放到哪里（如果有报插件缺失，去把kettle官方下载压缩包中的plugins复制到这里）
```

##### kettle变量
如果kettle中有需要用到变量的地方，需配置KETTLE_HOME环境变量，并在KETTLE_HOME目录中新建一个.kettle/kettle.properties文件放置环境变量信息



##### 编译打包
```
mvn install -Dmaven.test.skip=true

# 如果下载kettle相关依赖失败，可尝试指定settings
mvn install -Dmaven.test.skip=true -s settings.xml
```


#### 前端vue、element-ui

##### 安装依赖
```
npm i

npm run build
```

##### 测试环境
修改webpack.config.js，指向后端地址

```
devServer: {
    port: 8020,
    host: '127.0.0.1',
    historyApiFallback: true,
    hot: true,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8025',  // 后端地址
        changeOrigin: true,
        secure: true,
        pathRewrite: {'^/api': ''}
      }
    }
}
```

启动
```
npm run dev
```


##### 生产环境

打包
```
npm run build
```

生产环境建议使用NGINX作为WEB服务，配置示例：

新建目录：/kettle-task-web

添加kt.conf配置
```
server {
    listen 8025;
    root /kettle-task-web;


    location /dist {
        root  /app/kettle-task-web;
    }

    location / {
        try_files $uri $uri/ /index.html;
    }



    location /api {
        rewrite ^/api(/.*)$ $1 break;
        proxy_pass http://127.0.0.1:8025;
        proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
    }
}
```




示例图：

![image](https://github.com/liangalien/kettle-task/blob/main/images/%E8%B5%84%E6%BA%90%E7%AE%A1%E7%90%86.png?raw=true)


![image](https://github.com/liangalien/kettle-task/blob/main/images/%E4%BB%BB%E5%8A%A1%E8%AE%B0%E5%BD%951.png?raw=true)

![image](https://github.com/liangalien/kettle-task/blob/main/images/%E4%BB%BB%E5%8A%A1%E8%AE%B0%E5%BD%952.png?raw=true)

![image](https://github.com/liangalien/kettle-task/blob/main/images/%E4%BB%BB%E5%8A%A1%E9%9B%86%E6%88%901.png?raw=true)

![image](https://github.com/liangalien/kettle-task/blob/main/images/%E4%BB%BB%E5%8A%A1%E9%9B%86%E6%88%902.png?raw=true)
