var path;
var loaded;
var expanded = false;

$(document).ready(function() {
	$(window).on('popstate', function() {
		location.reload(true);
	});
	$('.loading').css({ display: 'grid' });
	if(loaded == true) {
		$('.loading').fadeOut(500);
	}
	$('.header').addClass('hidden');
});

$(window).on('load',function() {
	loaded = true;
	$('.loading').fadeOut(500);
});

function expand() {
	if(expanded) {
		$('.expandWrapper').fadeTo(200, 0);
		$('.header').addClass('hidden');
		setTimeout(function a() {
			$('.expand').removeClass('expanded');
			$('.expand').addClass('unexpanded');
		}, 200);
		setTimeout(function a() {$('.expandWrapper').fadeTo(200, 1);}, 500);
		expanded = false;
	} else {
		$('.expandWrapper').fadeTo(200, 0);
		$('.header').removeClass('hidden');
		setTimeout(function a() {
			$('.expand').removeClass('unexpanded');
			$('.expand').addClass('expanded');
		}, 200);
		setTimeout(function a() {$('.expandWrapper').fadeTo(200, 1);}, 500);
		expanded = true;
	}
}

function hideAll() {
	$('.page').hide();
}

function showPage(name, secondName, title, address, addressPath) {
	if(!$('.'+name+'.page').length) {
		$('.loading').show();
		$.ajax({
			url: path+address,
			type: 'GET',
			data: 'target=body&path='+path,
			success: function(a) {
				hideAll();
				$('.pageArea').append(a);
				$('.'+name).show();
				if(secondName != '') {
					$('.'+secondName).show();
				}
				window.history.pushState(address, title+" | einnfeigr", path+address);
				document.title = title+' | einnfeigr';
				path = addressPath;
				$('.loading').fadeOut(500);
			},
			error: function() {
				$('.loading').fadeOut(500);
			}
		});
	} else {
		hideAll();
		window.history.pushState(address, title+" | einnfeigr", path+address);
		document.title = title+' | einnfeigr';
		$('.'+name).show();
		if(secondName != '') {
			$('.'+secondName).show();
		}
		path = addressPath;
	}
}