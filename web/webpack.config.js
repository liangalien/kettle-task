const path = require('path');
const { DefinePlugin } = require('webpack');
const { VueLoaderPlugin } = require('vue-loader');
const HtmlWebpackPlugin = require('html-webpack-plugin');


module.exports = {
  mode: 'development',
  entry: {
    'kt-web': './src/main.js'
  },
  output: {
    filename: '[name].js',
    path: path.resolve('dist'),
    publicPath: '/'
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        use: ['vue-loader']
      },
      {
        test: /\.js$/,
        use: ['babel-loader']
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader', 'postcss-loader']
      },
      {
        test: /\.less$/,
        use: ['style-loader', 'css-loader', 'less-loader']
      },
      {
        test: /\.(png|jpg|jpeg|gif|svg|svgz|woff|woff2|eot|ttf|otf)(\?.+)?$/,
        type: 'asset/inline'
      }
    ]
  },
  plugins: [
    new VueLoaderPlugin(),
    new DefinePlugin({
      BASE_URL: JSON.stringify('/')
    }),
    new HtmlWebpackPlugin({
      template: path.resolve('src/index.html'),
      minify: {
        removeComments: true, // 移除HTML中的注释
        collapseWhitespace: true, // 删除空符与换符
        minifyCSS: true // 压缩内联css
      }
    })
  ],
  optimization: {
    splitChunks: {
      chunks: 'all',
      minChunks: 3,
      maxAsyncRequests: 5,
      maxInitialRequests: 5,
      cacheGroups: {
        vendor: {
          test: /node_modules/,
          chunks: 'all',
          name: 'vendor',
          priority: 10, // 优先级
          enforce: true
        },
        main: {
          test: /src/,
          name: 'atm-web-plugin-vue',
          enforce: true
        }
      }
    }
  },
  resolve: {
    extensions: ['.js', '.json', '.scss', '.css', '.vue'],
    alias: {
      '@': path.resolve(__dirname, 'src'),
      '@theme': path.resolve(__dirname, 'src/theme'),
      '@utils': path.resolve(__dirname, 'src/utils'),
      '@components': path.resolve(__dirname, 'src/components'),
      '@pages': path.resolve(__dirname, 'src/pages'),
      '@images': path.resolve(__dirname, 'src/images')
    }
  },
  devServer: {
    port: 8020,
    host: '127.0.0.1',
    historyApiFallback: true,
    hot: true,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8025',
        changeOrigin: true,
        secure: true,
        pathRewrite: {'^/api': ''}
      }
    }
  }
};
