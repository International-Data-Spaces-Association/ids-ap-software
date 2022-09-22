/**
 * Configuration template file to bind specific properties to environment variables.
 * All values must have the prefix $VUE_APP_.
 * Their corresponding environment variable key labels must be the values without the $ character.
 * This object should be structurally identical (name and path) to the standard configuration file.
 */
export default {
  VUE_APP_SHACL_API_URL: '$VUE_APP_SHACL_API_URL',
  VUE_APP_CORSPROXY_API_URL: '$VUE_APP_CORSPROXY_API_URL',
  VUE_APP_PIWIK_ID: '$VUE_APP_PIWIK_ID',
};
