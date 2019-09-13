/**
 * Function for iterating the object to check
 * whether it is empy
 * @param {*} obj object to be validated
 */
export const isEmpty = obj => {
  for (var key in obj) {
    if (obj.hasOwnProperty(key)) return false;
  }
  return true;
};
