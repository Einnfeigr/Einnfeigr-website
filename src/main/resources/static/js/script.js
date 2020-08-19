var path;
var loaded;

$(document).ready(function() {
	$(window).on('popstate', function() {
		location.reload(true);
	});
	$('.loading').css({ display: 'grid' });
	if(loaded == true) {
		$('.loading').fadeOut(500);
	}
});

$(window).on('load',function() {
	loaded = true;
	$('.loading').fadeOut(500);
});


function showPage(title, address, addressPath) {
	$('.page').remove();
	$('.loading').show();
	$.ajax({
		url: path+address,
		type: 'GET',
		data: 'target=body&path='+path,
		success: function(a) {
			$('.pageArea').append(a);
			var ver = '';
			var version = checkVersion();
			if(version != '') {
				ver = '?ver='+version;
			}
			window.history.pushState(address, title+" | einnfeigr", path+address+ver);
			document.title = title+' | einnfeigr';
			path = addressPath;
			$('.loading').fadeOut(500);
		},
		error: function() {
			$('.loading').fadeOut(500);
		}
	});
}

function checkVersion() {
	if(window.location.href.indexOf('ver=') < 0) {
		return '';
	}
	if($('.nav').length) {
		return 'mobile';
	} else {
		return 'desktop';
	}
}

function switchVersion(ver) {
	var url = window.location.href;
	if(url.indexOf('ver=') > -1) {
		url = removeURLParameter(url, 'ver');
	}
	if (url.indexOf('?') > -1) {
		url += '&';
	} else {
		url += '?';
	}
	if(ver == 'mobile') {
		url += 'ver=mobile';
	} else if(ver == 'desktop') {
		url += 'ver=desktop';
	} else {
		return;
	}
	window.location.href = url;	
}

function removeURLParameter(url, parameter) {
    var urlparts = url.split('?');   
    if (urlparts.length < 2) {
    	return url;
    }
    var prefix = encodeURIComponent(parameter) + '=';
    var pars = urlparts[1].split(/[&;]/g);
    for (var i = pars.length; i-- > 0;) {    
        if (pars[i].lastIndexOf(prefix, 0) !== -1) {  
        	pars.splice(i, 1);
        }
    }
    return urlparts[0] + (pars.length > 0 ? '?' + pars.join('&') : '');
}