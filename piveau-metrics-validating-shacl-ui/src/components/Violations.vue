<template>
  <v-card
    class="violations"
    elevation="10"
    :loading="$store.state.isLoading"
  >
    <v-card-title style="display:flex;">
      <v-row class="px-3">
        <v-col>
          <h2 class="text-h5 text-primary font-weight-bold mt-5 pt-7">
            Results
          </h2>
        </v-col>
      </v-row>
    </v-card-title>

    <v-card-text class="violations__body-container">
      <v-container
        fluid
        class="px-0"
      >
        <v-row dense>
          <!-- SUCCESS/ERROR STATE -->
          <v-col v-if="!getLoading && !isInitialState && violationsSuccess">
            <v-alert
              outlined
              :type="getRequestError ? 'error' : 'success'"
              text
            >
              <span v-if="getRequestError">
                {{ getRequestError }}
              </span>
              <span v-else>
                Your API definition has no violations
              </span>
            </v-alert>
          </v-col>
          <!-- EMPTY STATE -->
          <v-col v-else-if="isInitialState && violations.length === 0">
            <div class="text-center py-5 mt-5">
              Validation results will be displayed here
            </div>
          </v-col>
          <v-col
            v-for="(violation, index) in violations"
            v-else
            :key="index"
            cols="12"
          >
            <v-card
              elevation="5"
              class="violation-card"
            >
              <v-card-title class="d-sm-flex justify-sm-space-between">
                <div class="text-body-2 font-weight-bold">
                  <v-chip
                    class="ma-2"
                    :color="resultColors[violation.resultSeverity]"
                  >
                    {{ violation.resultSeverity.replace('sh:', '') }}
                  </v-chip>
                </div>
              </v-card-title>

              <v-card-subtitle>
                <span class="text--darken-1">
                  Issue in property
                  <strong>{{ violation.resultPath }}</strong>
                  <br>
                  of node
                  <a :href="violation.focusNode">{{ violation.focusNode }}</a>
                </span>
              </v-card-subtitle>

              <v-card-text>
                <strong class="text-primary mb-3">
                  {{ violation.resultMessage }}
                </strong>
                <br>
                but is
                <strong class="text-primary mb-3">
                  {{ violation.value || violation['sh:value'] }}
                </strong>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-card-text>
  </v-card>
</template>

<script>

import { mapGetters } from 'vuex';

export default {
  name: 'Violations',

  props: {
    violations: {
      type: Array,
      default: () => [],
    },
    violationsCount: {
      type: Number,
      default: 0,
    },
    validationSource: {
      // Used to determine whether something has been validated yet
      // Can be either 'editor', 'url', or null
      // Useful for jumping to specific code lines when validating with editor
      type: String,
      default: null,
    },
  },

  data() {
    return {
      resultColors: {
        'sh:Violation': 'error',
        'sh:Warning': 'warning',
        'sh:Info': 'info',
      },
    };
  },

  computed: {
    ...mapGetters('violations', [
      'getViolations',
      'getLoading',
      'getRequestError',
    ]),
    violationsSuccess() {
      return this.violationsCount === 0;
    },
    // Returns existing violation type keys
    nonZeroViolationKeys() {
      return Object.keys(this.violationsCount).filter((v) => this.violationsCount[v] > 0);
    },
    isInitialState() {
      // Once we have a validationSource (i.e editor or url) we know
      // that the state is not initial anymore
      return !this.validationSource;
    },
  },

  watch: {
    getRequestError(error) {
      if (error) {
        this.$store.dispatch('snackbar/showError', error);
      }
    },
  },

  methods: {
    parseViolationLine(start, end) {
      const line = `Line ${start}`;
      return !end || start === end
        ? line
        : `${line}-${end}`;
    },
    parseViolationPointer(pointer) {
      const display = pointer
        .replace(/^\//g, '')
        .replace(/\//g, ' > ')
        .replace(/~1/g, '/')
        .replace(/~0/g, '~');
      return display;
    },
  },
};
</script>

<style lang="scss" scoped>
  @import '~vuetify/src/styles/styles.sass';
  .violations {
    &__body-container {
      max-height: 800px;
      overflow: auto;
    }
  }

  h2 {
    text-align: center !important;
  }
</style>
