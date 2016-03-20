var rest = function(uuid, url) {
	$.ajax({
		url: url,
		success: function(msg) {
			$(document).ajaxComplete(function(event, request, settings) {
				location.reload();
			});
		},
		error: function(jqXHR, textStatus, errorThrown) {
			var l = jqXHR.responseText.length;
			return callback(false, jqXHR.responseText);
		}
	});
};

var post = function(url, callback) {
	$.ajax({	
		type: "POST",
		url: url,
		success: function(msg) {
			return callback(true);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			var l = jqXHR.responseText.length;
			return callback(false, jqXHR.responseText);
		}
	});
};

var postForm = function(url, data, callback) {
	$.ajax({	
		type: "POST",
		url: url,
		data: data,
		success: function(msg) {
			return callback(true, msg);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			var l = jqXHR.responseText.length;
			return callback(false, jqXHR.responseText);
		}
	});
};

var put = function(url, callback) {
	$.ajax({	
		type: "PUT",
		url: url,
		success: function(msg) {
			return callback(true);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			var l = jqXHR.responseText.length;
			return callback(false, jqXHR.responseText);
		}
	});
};

var postDelete = function(url, callback) {
	$.ajax({	
		type: "DELETE",
		url: url,
		success: function(msg) {
			return callback(true);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			var l = jqXHR.responseText.length;
			return callback(false, jqXHR.responseText);
		}
	});
};

var showError = function(errorText, basePath) {
	var url = basePath + '/error';
	var form = $('<form action="' + url + '" method="post">' +
	  '<input type="hidden" name="errorDetails" value="' + escape(errorText) + '" />' +
	  '</form>');
	$('body').append(form);
	form.submit();
}

var redirect = function(url) {
	$.ajax({
		url: url,
		success: function(msg) {
			$(document).ajaxComplete(function(event, request, settings) {
				location.reload();
			});
		}
	});
};

var goback = function() {
	window.location.replace(document.referrer);
};                                  

String.prototype.trunc = String.prototype.trunc || function(n) {
    return this.length > n ? this.substr(0, n - 1) + '&hellip;' : this;
};

var jqid = function(id) {
	return id.replace( /(:|\.|\[|\])/g, "\\$1" );
}

