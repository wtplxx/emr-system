const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/doctor': {
        target: 'http://localhost:8181',
        changeOrigin: true
      },
      '/patient': {
        target: 'http://localhost:8181',
        changeOrigin: true
      },
      '/record': {
        target: 'http://localhost:8181',
        changeOrigin: true
      },
      '/authorize': {
        target: 'http://localhost:8181',
        changeOrigin: true
      }
    }
  }
})
