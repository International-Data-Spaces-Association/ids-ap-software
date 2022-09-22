/**
 * Utility functions to be used everywhere
 */

import mime from 'mime-types';

/**
 * Reads a file and returns a Promise returning its content in UTF-8 encoding.
 * @param {File} file the input file
 */
export function readFileAsync(file) {
  if (window?.File?.text) return file.text();

  // Fallback to FileReader for browsers that don't support File API like IE11
  // See https://caniuse.com/fileapi
  return new Promise((resolve, reject) => {
    const fileReader = new FileReader();
    fileReader.onload = () => resolve(fileReader.result);
    fileReader.onerror = () => reject(new Error(`Error occurred reading file: ${file.name}`));
    fileReader.onabort = () => reject(new Error(`Reading file ${file.name} aborted`));
    fileReader.readAsText(file);
  });
}

/**
 * Returns a Promise containing the MIME type of a given File
 * @param {File} file the input file
 * @param {string} fallback the fallback content type in case no file extension was detected.
 *                          Defaults to `'application/rdf+xml'`
 * @returns {Promise<string>}
 */
export function getContentTypeByFile(file, fallback = 'application/rdf+xml') {
  const filename = file.name;
  return mime.lookup(filename) || fallback;
}

/**
 * Returns a promise that resolves after a given time
 * @param {Number} ms time to sleep in milliseconds
 *
 * @returns {Promise<void>}
 */
export function sleep(ms = 0) {
  return new Promise((resolve) => {
    setTimeout(resolve, ms);
  });
}
