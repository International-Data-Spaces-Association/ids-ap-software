// const CompressionPlugin = require('compression-webpack-plugin');
// const zlib = require('zlib');

module.exports = {
  publicPath: '/',
  devServer: {
    host: 'localhost',
  },
  runtimeCompiler: true,
  transpileDependencies: ['vuetify'],
  /* configureWebpack: {
    plugins: [
      new CompressionPlugin({
        filename: '[path][base].br',
        algorithm: 'brotliCompress',
        test: /\.(js|css|html|svg)$/,
        compressionOptions: {
          params: {
            [zlib.constants.BROTLI_PARAM_QUALITY]: 11,
          },
        },
        threshold: 10240,
        minRatio: 0.8,
      }),
      new CompressionPlugin({
        filename: '[path][base].gz',
        algorithm: 'gzip',
        test: /\.js$|\.css$|\.html$|\.svg$/,
        threshold: 10240,
        minRatio: 0.8,
      }),
    ],
  }, */
};
