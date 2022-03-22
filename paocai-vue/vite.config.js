// import AutoImport from 'unplugin-auto-import/vite'
// import Components from 'unplugin-vue-components/vite'
// import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import {
	defineConfig,
	UserConfig
} from 'vite'
import vue from '@vitejs/plugin-vue'
import * as path from 'path'

const resolve = (p) => {
	return path.resolve(__dirname, p);
};


// https://vitejs.dev/config/
export default defineConfig({
	server: {
		port: 8888
	},
	plugins: [
		vue()
	],
	resolve: {
		// 配置路径别名
		alias: {
			'@': resolve('./src'),
			assets: resolve('./src/assets'),
			components: resolve('./src/components'),
			views: resolve('./src/views'),
		},
	},
})
