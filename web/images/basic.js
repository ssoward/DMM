/*
 * SimpleModal Basic Modal Dialog
 * http://www.ericmmartin.com/projects/simplemodal/
 * http://code.google.com/p/simplemodal/
 *
 * Copyright (c) 2010 Eric Martin - http://ericmmartin.com
 *
 * Licensed under the MIT license:
 *   http://www.opensource.org/licenses/mit-license.php
 *
 * Revision: $Id: basic.js,v 1.3 2011/03/29 01:08:21 ssoward Exp $
 */

jQuery(function ($) {
	// Load dialog on page load
	//$('#basic-modal-content').modal();

	// Load dialog on click
	$('#basic-modal .basic').click(function (e) {
		$('#basic-modal-content').modal();

		return false;
	});
});

jQuery(function ($) {
	// Load dialog on page load
	//$('#basic-modal-content').modal();
	
	// Load dialog on click
	$('#basic-modal4 .basic').click(function (e) {
		$('#basic-modal4-content').modal();
		
		return false;
	});
});
jQuery(function ($) {
	// Load dialog on page load
	//$('#basic-modal-content').modal();
	
	// Load dialog on click
	$('#basic-modal2 .basic').click(function (e) {
		$('#basic-modal2-content').modal();
		
		return false;
	});
});
jQuery(function ($) {
	// Load dialog on page load
	//$('#basic-modal-content').modal();
	
	// Load dialog on click
	$('#basic-modal1 .basic').click(function (e) {
		$('#basic-modal1-content').modal();
		
		return false;
	});
});
