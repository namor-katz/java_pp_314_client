let form = $("#userForm");
let modal = $("#userModal");

function ajaxDelete(id) {
    $.ajax({
        url: "http://localhost:8080/api/v1/user/delete/" + id,
        type: "DELETE"
    }).done(function () {
        location.reload(true)
    })
};


function ajaxGet(id) {
    $("#userModalLabel").html("Edit Юзер!");
    $.get("http://localhost:8080/api/v1/user/info/" + id,
        function (data) {
            $.each(data, function (key, value) {
                form.find("input[name='" + key + "']").val(value);
            });
        }).done(function () {
        modal.modal();
    });
}


function ajaxSave() {
    let url = "http://localhost:8080/api/v1/user/new";
    let type = "POST";
    let formData = {
        user: {
            id: $("#id").val(),
            username: $("#username").val(),
            email: $("#email").val(),
            maxweight: $("#maxweight").val(),
            password: $("#password").val(),
        },
    };

    if (formData.user.id == undefined) {
        formData.user.id = 0;
    }

    else if (formData.user.id !== "") {
        url = "http://localhost:8080/api/v1/user/update/" + formData.user.id;
        type = "PUT";
    }

    else {
        formData.user.id = 0;
    }

    $.ajax({
        url: url,
        type: type,
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        cache: false,
        data: JSON.stringify(formData.user)
    }).always(function () {
        // modal.modal("hide");
        // location.reload(true);
        let url2 = "http://localhost:8080/admin/list";
        $(location).attr('href',url2);
    });
}
