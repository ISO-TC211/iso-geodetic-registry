var rest = function(uuid, url) {
	$.ajax({
		url: url,
		success: function(msg) {
			$(document).ajaxComplete(function(event, request, settings) {
				location.reload();
			});
		}
	});
};

var post = function(url, callback) {
	$.ajax({	
		type: "POST",
		url: url,
		success: function(msg) {
			return callback();
		}
	});
};

var postForm = function(url, data, callback) {
	$.ajax({	
		type: "POST",
		url: url,
		data: data,
		success: function(msg) {
			return callback();
		}
	});
};

var put = function(url, callback) {
	$.ajax({	
		type: "PUT",
		url: url,
		success: function(msg) {
			return callback();
		}
	});
};

var postDelete = function(url, callback) {
	$.ajax({	
		type: "DELETE",
		url: url,
		success: function(msg) {
			return callback();
		}
	});
};

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