<template>
  <v-card class="editor-tab">
    <v-card-title>
      <v-container
        class="api-checker-container pa-0"
        fluid
      >
        <v-row align="center">
          <v-col
            class="shacl-validator-button-wrapper"
            cols="12"
          >
            <h1 class="text-h5 font-weight-bold mr-5 pb-2">
              SHACL Validator
            </h1>
            <div class="d-flex">
              <v-btn
                primary
                class="mr-1"
                color="#008469"
                @click="loadExample"
              >
                Load Example
              </v-btn>
              <v-btn
                primary
                :loading="$store.state['violations'].isLoading"
                :disabled="$store.state['violations'].isLoading"
                color="#008469"
                @click="$emit('validate-schema', localModel.code, localModel.type);"
              >
                Validate
              </v-btn>
            </div>
          </v-col>
        </v-row>
        <v-row align="center">
          <v-col
            class="d-flex"
            cols="6"
            sm="6"
          >
            <small style="margin: auto 0; margin-right: 10px; white-space: nowrap;">
              Validating:
            </small>
            <v-select
              v-model="localModel.type"
              :items="items"
            />
          </v-col>
        </v-row>
      </v-container>
    </v-card-title>

    <v-card-text class="pt-0 px-0">
      <codemirror
        v-model="localModel.code"
        class="api-editor"
        :options="cmOptions"
        @input="onCmChange"
      />
    </v-card-text>
  </v-card>
</template>

<script>
import { codemirror } from 'vue-codemirror';

import EXAMPLE_CODE from '@/assets/SHACL_violations.json';

// import base style
import 'codemirror/lib/codemirror.css';

// Syntax highlighting
import 'codemirror/mode/javascript/javascript';
import 'codemirror/mode/yaml/yaml';

// Linting
import 'codemirror/addon/lint/lint.css';
import 'codemirror/addon/lint/lint';
import 'codemirror/addon/lint/yaml-lint';
import 'codemirror/addon/lint/json-lint';

// Additional resources
import 'codemirror/addon/selection/active-line';

export default {
  name: 'EditorTab',
  components: {
    codemirror,
  },
  props: {
    // Parent components may use v-model
    value: {
      type: Object,
      required: true,
    },
    items: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      exampleCode: JSON.stringify(EXAMPLE_CODE),
      cmOptions: {
        tabSize: 4,
        mode: 'text/x-yaml',
        theme: 'default',
        lineNumbers: true,
        line: true,
        lint: false,
        styleActiveLine: true,
        foldGutter: true,
        gutters: [
          'CodeMirror-linenumbers',
          'CodeMirror-lint-markers',
          'CodeMirror-foldgutter',
        ],
      },
    };
  },

  computed: {
    // Parent components may use v-model
    localModel: {
      get() { return this.value; },
      set(localModel) {
        this.$emit('input', localModel);
      },
    },
  },

  methods: {
    // Returns true, if code is json-like
    isJson(code) {
      const jsonRegex = /^[ \r\n\t]*[{[]/;
      return jsonRegex.test(code);
    },
    onCmChange() {
      // Reactively swap syntax highlighting and linting depending
      this.cmOptions.mode = this.isJson(this.localModel.code) ? 'application/json' : 'text/x-yaml';
    },
    loadExample() {
      this.localModel.code = this.exampleCode;
      this.localModel.type = 'application/ld+json';
    },
  },
};
</script>

<style lang="scss">
  @import '../style/custom-styles.css';

  .CodeMirror {
    border-top: 1px solid rgba(0,0,0,.125);
    height: 800px;
    max-height: 800px;
  }
</style>
