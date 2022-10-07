<template>
  <v-container
    class="api-checker-container pa-0"
    fluid
  >
    <v-row class="content-boxes mobile-row">
      <!-- SUBMISSION -->
      <v-col
        class="mobile-col"
        cols="12"
        lg="8"
      >
        <v-card elevation="10">
          <v-tabs
            v-model="tab"
            color="primary"
            background-color="transparent"
            grow
          >
            <v-tab key="url">
              Data Provision
            </v-tab>

            <v-tab key="editor">
              Editor
            </v-tab>
          </v-tabs>
          <v-tabs-items v-model="tab">
            <v-snackbar
              v-model="snackbar.show"
              :timeout="snackbar.timeout"
              :color="snackbar.color"
            >
              {{ snackbar.text }}
              <template v-slot:action="{ attrs }">
                <v-btn
                  color="white"
                  text
                  v-bind="attrs"
                  @click="snackbar.show = false"
                >
                  Close
                </v-btn>
              </template>
            </v-snackbar>
            <!-- SUBMISSION URL -->
            <v-tab-item key="url">
              <UrlTab
                  @edit-in-editor="handleEditInEditor"
                  @validate-in-editor="handleValidateInEditor"
              />
            </v-tab-item>
            <!-- SUBMISSION EDITOR -->
            <v-tab-item key="editor">
              <EditorTab
                v-model="editorModel"
                :items="items"
                @validate-schema="validateSchema"
              />
            </v-tab-item>
          </v-tabs-items>
        </v-card>
      </v-col>
      <!-- VIOLATIONS -->
      <v-col
        class="mobile-col"
        cols="12"
        lg="4"
      >
        <Violations
          :violations="processedViolations"
          :violations-count="processedViolationsCount"
          :violations-success="violationsSuccess"
          :validation-source="validationSource"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>

import UrlTab from '@/components/UrlTab.vue';
import EditorTab from '@/components/EditorTab.vue';
import Violations from '@/components/Violations.vue';

import { mapGetters } from 'vuex';
import { readFileAsync } from '@/utils/helpers';
// import { config as CONFIG } from '../../config/config';

export default {
  name: 'Main',

  components: {
    UrlTab,
    EditorTab,
    Violations,
  },

  metaInfo() {
    return {
      title: 'IDS Connector Validator',
      description: 'IDS Connector Validator',
    };
  },

  data: () => ({
    tab: 0,
    items: [
      'text/turtle',
      'application/ld+json',
      'application/rdf+xml',
      'application/n-triples',
      'application/trig',
      'text/n3',
      'text/plain',
    ],
    snackbar: {
      show: false,
      text: '',
      color: '',
      timeout: -1,
    },
    editorModel: {
      code: '',
      type: 'text/turtle',
    },
    validationSource: null,
  }),

  computed: {
    ...mapGetters('violations', [
      'getViolations',
      'getViolationsSuccess',
      'getLoading',
    ]),
    violations() {
      return this.getViolations;
    },
    processedViolations() {
      return this.violations.filter((e) => e.resultSeverity);
    },
    processedViolationsCount() {
      return this.processedViolations.length;
    },
    violationsSuccess() {
      return this.getViolationsSuccess;
    },
    isLoading() {
      return this.getLoading;
    },
  },

  created() {
    this.$store.subscribe((mutation, state) => {
      if (mutation.type === 'snackbar/SHOW_MESSAGE') {
        const { text, color, timeout } = state.snackbar;
        this.snackbar.text = text;
        this.snackbar.color = color;
        this.snackbar.timeout = timeout;
        this.snackbar.show = true;
      }
    });
  },

  methods: {
    readFileAsync,
    /**
     * Validates API by schema
     */
    validateSchema(schema, type, param) {
      this.$store.dispatch('violations/getViolationsBySchema', { schema, type, param });
      this.validationSource = 'editor';
    },
    async handleEditInEditor(schema, type) {
      this.tab = 1;
      await this.$nextTick();
      this.editorModel.code = typeof schema === 'string' ? schema : JSON.stringify(schema);
      this.editorModel.type = type;
    },
    async handleValidateInEditor(schema, type) {
      await this.handleEditInEditor(schema, type);
      this.validateSchema(schema, type);
    },

    // Returns a filtered violations list referring to a whitelist of violation codes
    filterViolationsByCodes(violations, whitelist) {
      if (!violations) return null;

      // eslint-disable-next-line max-len
      let filteredViolations;

      if (whitelist) {
        filteredViolations = violations.filter((v) => {
          if (!v.rule_link) return false;

          const ruleUrl = new URL(v.rule_link);
          const ruleNumber = parseInt(ruleUrl.hash.substr(1), 10);
          return whitelist.includes(ruleNumber);
        });
      }

      return filteredViolations;
    },
    // Returns a transformed violations list to one violation type
    transformViolationsToType(violations, type) {
      if (!violations) return null;

      return violations.map((v) => ({ ...v, violation_type: type }));
    },
  },
};
</script>

<style lang="scss" scoped>
  .content-boxes {
    padding: 0 20px;
    @media (max-width: 1263px) {
      padding: 0;
    }
  }

  .mobile-row {
    @media only screen and (max-width: 1263px) {
      max-width: 100%;
      margin: 0 !important;
    }
  }

  .mobile-col {
    @media only screen and (max-width: 1263px) {
      padding-left: 0;
      padding-right: 0;
    }
  }
</style>
