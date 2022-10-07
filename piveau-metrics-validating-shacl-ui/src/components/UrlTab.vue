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
              IDS-AP Validator
            </h1>
            <div class="d-flex">
              <v-btn
                primary
                :disabled="!valid || loadingStates.editor || loadingStates.validate"
                :loading="loadingStates.editor"
                class="mr-1"
                color="primary"
                @click="handleEdit"
              >
                Edit in Editor
              </v-btn>

              <v-btn
                primary
                :disabled="!valid || loadingStates.editor || loadingStates.validate"
                :loading="loadingStates.validate"
                color="primary"
                @click="handleValidate"
              >
                Validate
              </v-btn>
            </div>
          </v-col>
        </v-row>
        <v-form
          ref="form"
          v-model="valid"
          class="mt-5 url-form-container"
        >
          <v-row align="center">
            <v-col
              class="d-flex pb-0"
              cols="12"
              sm="12"
            >
              <div class="url-validation-text">
                <br><br>
                You can either drag and drop your IDS-AP self-description on the page in the box below, or
                select the file with the “Select Document” button, or – if the self-description is
                accessible online – paste the URL below. The self-description must be a valid
                linked data file (application/ld+json, application/rdf+xml, application/n-triples,
                application/trig, text/turtle, text/n3 or text/plain).
              </div>
            </v-col>
            <v-col
              class="d-flex pt-0"
              cols="12"
              sm="12"
            >
              <div @click="handleTextFieldClick" class="text-field-wrapper">
                <v-text-field
                  v-model="url"
                  :rules="urlRules"
                  :disabled="!!file"
                  placeholder="Paste URL here..."
                  :required="false"
                  color="primary"
                  @keydown.enter.prevent="preventFormSubmit"
                />
              </div>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <dropzone
                v-model="file"
                :label="''"
                :rules="fileRules"
              />
            </v-col>
          </v-row>
        </v-form>
      </v-container>
    </v-card-title>
  </v-card>
</template>

<script>
import Dropzone from '@/components/Dropzone.vue';

import { readFileAsync, getContentTypeByFile, sleep } from '@/utils/helpers';

export default {
  name: 'UrlTab',

  components: { Dropzone },

  data() {
    return {
      type: 'application/rdf+xml',
      items: [
        'text/turtle',
        'application/ld+json',
        'application/rdf+xml',
        'application/n-triples',
        'application/trig',
        'text/n3',
      ],
      valid: false,
      url: '',
      file: undefined,
      dropzone: {
        dragged: false,
      },
      files: [],
      loadingStates: {
        editor: false,
        validate: false,
      },
      urlRules: [
        (value) => {
          // Ignore this rule when there is already a file
          // When visitor provides both file and url then prioritize file
          if (this.file) return true;

          const pattern = /^https?:\/\/(www\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_+.~#?&//=]*)$/;
          return pattern.test(value) || 'Invalid URL (must start with "http://" or "https://").';
        },
      ],
      fileRules: [
        (file) => {
          if (!file) return true;
          const type = this.getContentTypeByFile(file);
          return this.items.includes(type) || `The file '${file.name}' does not point to a valid file of type: ${this.items.join(', ')}`;
        },
      ],
    };
  },

  computed: {
    // Prioritizes file validation in the rare case that the user
    // decides to provide both url and file
    resourceToValidate() {
      return this.file || this.url;
    },
  },

  watch: {
    async file(file) {
      if (file) this.type = this.getContentTypeByFile(file);
      await this.$nextTick();
      this.$refs.form.validate();
    },
  },

  methods: {
    readFileAsync,
    getContentTypeByFile,
    validate() {
      this.$refs.form.validate();
    },
    reset() {
      this.$refs.form.reset();
    },
    resetValidation() {
      this.$refs.form.resetValidation();
    },
    preventFormSubmit(event) {
      event.preventDefault();
    },
    async retrieveSchema(res, isFile = false) {
      let schema;
      let type;
      try {
        if (isFile) {
          const file = res;
          type = this.getContentTypeByFile(file);
          if (!this.items.includes(type)) throw new Error(`The file '${file.name}' does not point to a valid file of type: ${this.items.join(', ')}`);
          schema = await this.readFileAsync(file);
        } else {
          const url = res;
          const corsproxy = this.$corsproxyService;

          // Test if given URL points to a valid document
          type = (await corsproxy.head(url)).headers['content-type'];

          // Remove possible charset / boundary from content type
          const index = type.indexOf(';');
          if (index > -1) type = type.substring(0, index);

          if (!this.items.includes(type)) throw new Error(`The URL ${res} does not point to a valid file of type: ${this.items.join(', ')}`);

          const response = await corsproxy.get(url);
          schema = response.data;
        }
      } catch (e) {
        this.$store.dispatch('snackbar/showError', e);
      }

      return { schema, type };
    },
    // Loads a schema, manages its loading states, and emits an event with the corresponding data
    async loadSchemaAndEmit({ loadingType = 'editor', eventName = 'edit-in-editor' }) {
      const isFile = typeof this.resourceToValidate !== 'string';
      this.loadingStates[loadingType] = true;
      try {
        const { schema, type } = await this.retrieveSchema(this.resourceToValidate, isFile);
        if (schema && type) this.$emit(eventName, schema, type);
      } catch (e) {
        // eslint-disable-next-line no-console
        console.error(e);
        this.$store.dispatch('snackbar/showError', e);
      } finally {
        // Sleep a bit before enabling the buttons again due to transition time from
        // switching tabs
        await sleep(1000);
        this.loadingStates[loadingType] = false;
      }
    },
    async handleEdit() {
      await this.loadSchemaAndEmit({ loadingType: 'editor', eventName: 'edit-in-editor' });
    },
    async handleValidate() {
      await this.loadSchemaAndEmit({ loadingType: 'validate', eventName: 'validate-in-editor' });
    },
    handleTextFieldClick() {
      if (this.file) {
        this.$store.dispatch('snackbar/showError', 'If you want to insert a URL, please delete the file in the drag and drop field');
      }
    },
  },
};
</script>

<style lang="scss" scoped>
  @import '../style/custom-styles.css';

  .url-form-container {
    // Set a large enough height for the container
    // so that tab switches won't flicker
    min-height: 800px;
  }

  .url-validation-text {
    white-space: pre-line;
    font-size: 16px;
    font-weight: normal;
    letter-spacing: 0.0125em;
    line-height: 1.5;
    word-break: normal;
  }

  .text-field-wrapper {
    width: 100%;
  }
</style>
