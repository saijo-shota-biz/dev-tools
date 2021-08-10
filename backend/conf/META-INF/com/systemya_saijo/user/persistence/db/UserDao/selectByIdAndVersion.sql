SELECT
  *
FROM
  users u
WHERE
 u.id = /* id */'test'
 AND u.version = /* version */0
FOR UPDATE