var loaded;

$(document).ready(function() {
	$(".backgroundImage video").resume();
	$(window).on('popstate', function() {
		location.reload(true);
	});

	$('.previewBackground').removeClass('hidden');
	if(loaded == true) {
		$('.previewBackground').fadeOut(500, function() {
			$('.loading').hide();
		});
	}
});


$(window).on('load', function() {
	loaded = true;
	$('.previewBackground').fadeOut(500, function() {
		$('.loading').hide();
	});
});


function removeDiv(cl) {
	$('.'+cl).fadeOut(500);
	setTimeout(function() {
		$('.'+cl).remove();
	}, 500);
}

function showImage(image) {
	$('.loading').show();
	$('.previewBackground').fadeIn(50);
	$.ajax({
		url: '/image',
		type: 'GET',
		data: 'imgPath='+image,
		success: function(a) {
			$('.previewBackground').append(a);
			$('.loading').hide();
		},
		error: function() {
			$('.previewBackground').fadeOut(500, function() {
				$('.loading').hide();
			});
		}
	});
}

function hideImagePreview(id) {
	$('.previewBackground').fadeOut(500, function() {
		$('#'+id).remove();
	});
}

function showPage(address) {
	$('.loading').show();
	$('.previewBackground').fadeIn(50);
	var ver = '';
	var version = checkVersion();
	if(version != '') {
		ver = '?ver='+version;
	}
	$.ajax({
		url: address+ver,
		beforeSend: function(request) {
		    request.setRequestHeader("target", "body");
		},
		type: 'GET',
		success: function(a) {
			$('.page').remove();
			window.history.pushState(address, a.title+" | einnfeigr", address
					+ver);
			document.title = a.title+' | einnfeigr';
			document.body.scrollTop = 0;
			document.documentElement.scrollTop = 0;
			$('.pageArea').append(a.content);
			$('.previewBackground').fadeOut(500, function() {
				$('.loading').hide();
			});
		},
		error: function() {
			$('.previewBackground').fadeOut(500, function() {
				$('.loading').hide();
			});
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