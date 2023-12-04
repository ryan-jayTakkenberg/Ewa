// Added for deployment, I think it does nothing but didn't delete it yet. Old config is commented out below.

const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  publicPath: process.env.NODE_ENV === 'production' ? '/your-production-path/' : '/',
  transpileDependencies: ['vuetify'], // Add any other dependencies you want to transpile

});





// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true
// })