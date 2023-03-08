module.exports = {
  devServer: {
    host: "localhost",
    port: 8083  ,
    open: true,
    disableHostCheck: true,
    proxy: {
      "/api": {
        target: "http://localhost:8082", // 要跨域的域名
        changeOrigin: true, // 是否开启跨域
        pathRewrite: {
          "^/api": "",
        }
      },
    },
  },
};
