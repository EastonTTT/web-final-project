import pluginVue from 'eslint-plugin-vue'
import vueTsEslintConfig from '@vue/eslint-config-typescript'

export default [
  {
    name: 'app/files-to-lint',
    files: ['**/*.{ts,mts,tsx,vue}'],
  },

  {
    name: 'app/files-to-ignore',
    ignores: ['**/dist/**', '**/dist-ssr/**', '**/coverage/**'],
  },
  {
    // 禁用多单词组件名的规则
    'vue/multi-word-component-names': 'off'
  },

  ...pluginVue.configs['flat/essential'],
  ...vueTsEslintConfig(),
]
