
$(function () {
	
	  $("#logoutButton").click(function () {
		 
	        $.ajax({
	            url: "/logmessages",
	            type: "GET",
	            //contentType: "application/json; charset=utf-8",
	           // dataType: "json",
	            success: function (data) {
	                $("#divId1").val("dddddddddddddddddddddddddddddddddddddddddd");
	               
	            },
	            error: function (errorThrown) {
	            }
	        });
	    });
	 function test(){
		
		    
	 }

    function doLogin(loginData) {
        $.ajax({
            url: "/auth",
            type: "POST",
            data: JSON.stringify(loginData),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                console.log(data);
                setJwtToken(data.token);
                $login.hide();
                $notLoggedIn.hide();
                showTokenInformation();
                showUserInformation();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if (jqXHR.status === 401 || jqXHR.status === 403) {
                    $('#loginErrorModal')
                        .modal("show")
                        .find(".modal-body")
                        .empty()
                        .html("<p>Message from server:<br>" + jqXHR.responseText + "</p>");
                } else {
                    throw new Error("an unexpected error occured: " + errorThrown);
                }
            }
        });
    }

  

   
    
    // REGISTER EVENT LISTENERS =============================================================
    $("#loginForm").submit(function (event) {
        event.preventDefault();

        var $form = $(this);
        var formData = {
            username: $form.find('input[name="username"]').val(),
            password: $form.find('input[name="password"]').val()
        };

        doLogin(formData);
    });


   

 

   

});
