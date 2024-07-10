const path = require('path');
const { DefinePlugin } = require("webpack");
const { VueLoaderPlugin } = require("vue-loader");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CompressionPlugin = require("compression-webpack-plugin");

module.exports = {
  mode: "production",
  entry: {
    'kt-web': './src/main.js'
  },
  output: {
    filename: "[name].js",
    path: path.resolve("dist"),
    publicPath: "/",
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        use: ['vue-loader']
      },
      {
        test: /\.js$/,
        use: ['babel-loader'],
        //include: ['/src', '/node_modules/element-ui/src', '/node_modules/element-ui/packages'],
        //exclude: /node_modules/
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader', 'postcss-loader']
      },
      {
        test: /\.less$/,
        use: ['style-loader', 'css-loader', 'less-loader' ]
      },
      {
        test: /\.(png|jpg|jpeg|gif|svg|svgz|woff|woff2|eot|ttf|otf)(\?.+)?$/,
        type: "asset/inline"
      },
    ],
  },
  plugins: [
    new VueLoaderPlugin(),
    new DefinePlugin({
      BASE_URL: JSON.stringify("/"),
    }),
    new HtmlWebpackPlugin({
      template: path.resolve("src/index.html"),
      minify: {
        removeComments: true, // 移除HTML中的注释
        collapseWhitespace: true, // 删除空符与换符
        minifyCSS: true, // 压缩内联css
      },
    }),
    new CleanWebpackPlugin(),
    new CompressionPlugin({
      filename: "[path][base].gz",
      threshold: 10240,
      minRatio: 0.8,
    }),
      //new BundleAnalyzerPlugin()
  ],
  optimization: {
    splitChunks: {
      chunks: "all",
      minChunks: 3,
      maxAsyncRequests: 5,
      maxInitialRequests: 5,
      cacheGroups: {
        vendor: {
          test: /node_modules/,
          chunks: "all",
          name: "vendor",
          priority: 10, // 优先级
          enforce: true,
        },
        main: {
          test: /src/,
          name: "kt-web",
          enforce: true,
        },
      },
    },
  },
  resolve: {
    extensions: ['.js', '.json', '.scss', '.css', '.vue'],
    alias: {
      '@': path.resolve(__dirname, 'src'),
      '@theme': path.resolve(__dirname, 'src/theme'),
      '@utils': path.resolve(__dirname, 'src/utils'),
      '@components': path.resolve(__dirname, 'src/components'),
      '@pages': path.resolve(__dirname, 'src/pages'),
      '@images': path.resolve(__dirname, 'src/images'),
    }
  }
};
