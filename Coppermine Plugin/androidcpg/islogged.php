<?php

define('IN_COPPERMINE', true);
define('LOGIN_PHP', true);
chdir('../../');
require('include/init.inc.php');
require_once('androidcpg_enabled.php');

echo ((USER_ID  && isset($cpg_udb) && ($cpg_udb->session_extraction() !== false)) ? "true":"false");

?>
