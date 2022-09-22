<template>
  <div>
    <div>
      {{ label }}
    </div>
    <div
      class="dropzone rounded-xl pa-6"
      :class="{'dropzone--dragged': dragged}"
      @drop.prevent="handleFileDrop"
      @dragover.prevent="handleDragover"
      @dragenter.prevent="dragged=true"
      @dragleave="dragged=false"
    >
      <div class="dropzone__tip">
        <v-icon
          :size="100"
          color="grey lighten-2"
        >
          move_to_inbox
        </v-icon>
        <div class="d-flex align-items-center">
          <div class="grey--text">
            Drag-and-drop here or
          </div>
          <input
            v-show="false"
            ref="fileInput"
            type="file"
            @change.prevent="handleFileInput"
          >
          <v-btn
            class="mx-3"
            color="secondary"
            small
            @click.prevent="$refs.fileInput.click"
          >
            Select document
          </v-btn>
        </div>
      </div>
      <div
        v-if="file"
        class="d-flex align-items-center mt-5"
      >
        <v-icon :color="errorBucket.length > 0 ? 'error' : undefined">
          mdi-file
        </v-icon>
        <span :class="{ 'error--text': errorBucket.length > 0 }">
          {{ file ? file.name : '(no file)' }}
        </span>
        <v-btn
          icon
          color="secondary"
          class="ml-1"
          @click="reset"
        >
          <v-icon>mdi-delete</v-icon>
        </v-btn>
      </div>

      <div
        v-if="errorBucket.length > 0"
        class="d-flex align-items-center mt-5"
      >
        <VMessages
          :value="errorBucket"
          color="error"
        />
      </div>

      <!-- <div class="dropzone__overlay grey--text" /> -->
    </div>
  </div>
</template>

<script>
import VInput from 'vuetify/lib/components/VInput/VInput';

export default {
  name: 'Dropzone',

  // For better form component integration e.g. with rule validation
  extends: VInput,

  props: {
    value: {
      type: [Object, File],
      default: undefined,
    },
  },

  data() {
    return {
      // is true when a file is being hovered above the dropzone
      dragged: false,
      file: undefined,
    };
  },

  watch: {
    value: {
      immediate: true,
      deep: true,
      handler(value) {
        if (value) {
          this.file = value;
        }
      },
    },
  },

  methods: {
    handleDragover() {},
    handleDragenter() {},
    handleDragleave() {},
    handleFileDrop(event) {
      this.dragged = false;
      const droppedFiles = event.dataTransfer.files;
      const file = droppedFiles[0];
      if (file) this.$emit('input', file);
    },
    handleFileInput(event) {
      const files = event?.target?.files;
      const file = files.length > 0
        ? files[0]
        : undefined;

      if (file) this.$emit('input', file);
    },
    reset() {
      this.$refs.fileInput.value = '';
      this.file = undefined;
      this.$emit('input', undefined);
    },
  },
};
</script>

<style lang="scss" scoped>

  .dropzone {
    position: relative;
    min-height: 120px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    * {
      pointer-events: auto;
    }

    &--dragged {
      border: 3px dashed #202272;
      * {
        pointer-events: none;
      }
    }

    position: relative;
    border: 2px dashed #ccc;

    &__tip {
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    &__overlay {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
    }
  }
</style>
