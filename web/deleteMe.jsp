
<!DOCTYPE html> 
<html lang="en"> 
<head> 
	<meta charset="UTF-8" /> 
	<title>jQuery UI Tabs - Simple manipulation</title> 
	<link type="text/css" href="css/jquery.ui.all.css" rel="stylesheet" /> 
	<script type="text/javascript" src="js/jquery-1.4.2.js"></script> 
	<script type="text/javascript" src="js/jquery.ui.core.js"></script> 
	<script type="text/javascript" src="js/jquery.ui.widget.js"></script> 
	<script type="text/javascript" src="js/jquery.ui.button.js"></script> 
	<script type="text/javascript" src="js/jquery.ui.tabs.js"></script> 
	<script type="text/javascript" src="js/jquery.ui.dialog.js"></script> 
	<link type="text/css" href="css/demos.css" rel="stylesheet" /> 
	<style type="text/css"> 
		#dialog label, #dialog input { display:block; }
		#dialog label { margin-top: 0.5em; }
		#dialog input, #dialog textarea { width: 95%; }
		#tabs { margin-top: 1em; }
		#tabs li .ui-icon-close { float: left; margin: 0.4em 0.2em 0 0; cursor: pointer; }
		#add_tab { cursor: pointer; }
	</style> 
	<script type="text/javascript"> 
	$(function() {
		var $tab_title_input = $('#tab_title'), $tab_content_input = $('#tab_content');
		var tab_counter = 2;
 
		// tabs init with a custom tab template and an "add" callback filling in the content
		var $tabs = $('#tabs').tabs({
			tabTemplate: '<li><a href="#{href}">#{label}</a> <span class="ui-icon ui-icon-close">Remove Tab</span></li>',
			add: function(event, ui) {
				var tab_content = $tab_content_input.val() || 'Tab '+tab_counter+' content.';
				$(ui.panel).append('<p>'+tab_content+'</p>');
			}
		});
 
		// modal dialog init: custom buttons and a "close" callback reseting the form inside
		var $dialog = $('#dialog').dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				'Add': function() {
					addTab();
					$(this).dialog('close');
				},
				'Cancel': function() {
					$(this).dialog('close');
				}
			},
			open: function() {
				$tab_title_input.focus();
			},
			close: function() {
				$form[0].reset();
			}
		});
 
		// addTab form: calls addTab function on submit and closes the dialog
		var $form = $('form',$dialog).submit(function() {
			addTab();
			$dialog.dialog('close');
			return false;
		});
 
		// actual addTab function: adds new tab using the title input from the form above
		function addTab() {
			var tab_title = $tab_title_input.val() || 'Tab '+tab_counter;
			$tabs.tabs('add', '#tabs-'+tab_counter, tab_title);
			tab_counter++;
		}
 
		// addTab button: just opens the dialog
		$('#add_tab')
			.button()
			.click(function() {
				$dialog.dialog('open');
			});
 
		// close icon: removing the tab on click
		// note: closable tabs gonna be an option in the future - see http://dev.jqueryui.com/ticket/3924
		$('#tabs span.ui-icon-close').live('click', function() {
			var index = $('li',$tabs).index($(this).parent());
			$tabs.tabs('remove', index);
		});
	});
	</script> 
</head> 
<body> 
 
<div class="demo"> 
 
	<div id="dialog" title="Tab data"> 
		<form> 
			<fieldset class="ui-helper-reset"> 
				<label for="tab_title">Title</label> 
				<input type="text" name="tab_title" id="tab_title" value="" class="ui-widget-content ui-corner-all" /> 
				<label for="tab_content">Content</label> 
				<textarea name="tab_content" id="tab_content" class="ui-widget-content ui-corner-all"></textarea> 
			</fieldset> 
		</form> 
	</div> 
 
	<button id="add_tab">Add Tab</button> 
 
	<div id="tabs"> 
		<ul> 
			<li><a href="#tabs-1">One</a> <span class="ui-icon ui-icon-close">Remove Tab</span></li> 
		</ul> 
		<div id="tabs-1"> 
      one
		</div> 
	</div> 
 
</div><!-- End demo --> 
 
</body> 
</html> 