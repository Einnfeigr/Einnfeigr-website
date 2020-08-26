var loaded;

$(document).ready(function() {
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

$(window).on('loadstart', function() {
	$('.previewBackground').removeClass('hidden');
})

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

function showImagePreview(image) {
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

function showPage(title, address) {
	$('.loading').show();
	$('.previewBackground').fadeIn(50);
	$.ajax({
		url: address,
		type: 'GET',
		data: 'target=body',
		success: function(a) {
			var ver = '';
			var version = checkVersion();
			if(version != '') {
				ver = '?ver='+version;
			}
			$('.page').remove();
			window.history.pushState(address, title+" | einnfeigr", address+ver);
			document.title = title+' | einnfeigr';
			$('.pageArea').append(a);
			$('.previewBackground').fadeOut(500);
			setTimeout(500, function() {
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