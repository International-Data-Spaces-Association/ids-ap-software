<template>
  <v-card
      class="pa-md-2 mx-lg-auto"
      elevation="10"
  >
    <v-card-title>
            <h1 class="text-h5 font-weight-bold mr-5 pb-2">
              About this Tool
            </h1>
    </v-card-title>
    <v-card-text>
      This tool is based on the IDS Information Model Application Profiles. Detailed information can be
      found <a href="https://github.com/International-Data-Spaces-Association/IM-Application-Profiles" target="_blank">here</a>.
      The validator supports two levels of profiles. The basic validation covers the default IDS information model. The extended validation represents an extended version,
      that applies stricter constraints to the model, such as cardinalities. The code for this tool is published <a href="https://github.com/International-Data-Spaces-Association/ids-ap-software" target="_blank">here</a>.
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: 'Info.vue',
};
</script>

<style scoped>

</style>
