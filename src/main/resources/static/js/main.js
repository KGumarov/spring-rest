class Role {
    id;
    authority;
}

class User {
    id;
    firstName;
    lastName;
    age;
    username;
    password;
    authorities = [];
}

const url_users = '/admin/list';
const url_roles = '/admin/roles';
const url_save_user = '/admin/saveUser';
const url_delete_user = '/admin/deleteUser';
let url_user = '/user/getCurrentUser';

let admin = function () {

    return {
        getUsers: async function () {
            let users_json;
            let response = await fetch(url_users);
            if (response.ok) {
                users_json = await response.json();
            }
            return users_json;
        },

        getRoles: async function () {
            let roles_json;
            let response = await fetch(url_roles);
            if (response.ok) {
                roles_json = await response.json();
            }
            return roles_json;
        },

        getUser: async function () {
            let user_json;
            let response = await fetch(url_user);
            if (response.ok) {
                user_json = await response.json();
            } else {
                alert("Ошибка HTTP: " + response.status);
            }
            return user_json;
        },


        saveUser: async function (user) {
            let response = await fetch(url_save_user, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                },
                body: JSON.stringify(user)
            });
            if (response.ok) {
                await response;
            } else {
                alert("Ошибка HTTP: " + response.status);
            }

        },

        deleteUser: async function (user) {
            let response = await fetch(url_delete_user, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                },
                body: JSON.stringify(user)
            });
            if (response.ok) {
                await response;
            } else {
                alert("Ошибка HTTP: " + response.status);
            }

        }
    }
};


$(function () {
    let api = admin();

    function updateUsers() {
        let tbody = $('#body_users_table');
        let tbody_user = $('#body_user_table');
        let head_text = $('#header_text');
        let roles_text;
        tbody.empty();
        tbody_user.empty();

        //Admin
        api.getUsers().then(users_json => {
            for (let i = 0; i < users_json.length; i++) {
                let user = new User();
                user.id = users_json[i].id;
                user.firstName = users_json[i].firstName;
                user.lastName = users_json[i].lastName;
                user.age = users_json[i].age;
                user.username = users_json[i].username;
                roles_text = users_json[i].authorities.map(r => r.authority).map(r => r.replaceAll("ROLE_", "")).join(' ');

                let tr = $('<tr/>')
                    .append($('<td/>').text(users_json[i].id))
                    .append($('<td/>').text(users_json[i].firstName))
                    .append($('<td/>').text(users_json[i].lastName))
                    .append($('<td/>').text(users_json[i].age))
                    .append($('<td/>').text(users_json[i].username))
                    .append($('<td/>').append($('<span/>').text(roles_text)))
                    .append($('<td/>').append('<button type="button" class="btn btn-primary" data-user = ' + JSON.stringify(user) + ' data-toggle="modal" data-target="#modal_edit">Edit</button>'))
                    .append($('<td/>').append('<button type="button" class="btn btn-danger"  data-user = ' + JSON.stringify(user) + '  data-toggle="modal" data-target="#modal_delete">Delete</button>')
                    );
                tbody.append(tr);
            }
        });

        //roles to new_user & modals
        api.getRoles().then(roles_json => {
            $('#user_roles_new_user').find('option').remove();
            $('#user_roles_update_modal').find('option').remove();
            $('#user_roles_delete_modal').find('option').remove();

            let roles = $('#user_roles_new_user');
            $.each(roles_json, function (key, value) {
                roles.append('<option value="' + value.id + '">' + value.authority.replaceAll("ROLE_", "") + '</option>');
            });
            roles = $('#user_roles_delete_modal');
            $.each(roles_json, function (key, value) {
                roles.append('<option value="' + value.id + '">' + value.authority.replaceAll("ROLE_", "") + '</option>');
            });
            roles = $('#user_roles_update_modal');
            $.each(roles_json, function (key, value) {
                roles.append('<option value="' + value.id + '">' + value.authority.replaceAll("ROLE_", "") + '</option>');
            });
        })

    }



    //push new user
    $('#button_new_user').click(function () {
        let new_user = new User();

        $('#newuser .form-control').each(function (index, element) {
            new_user[element.name] = element.value;
        });

        new_user.age = Number(new_user.age)
        new_user.id = 0;

        //pick selected roles
        let userRolesSelect = $('#user_roles_new_user');
        let selected_roles = userRolesSelect.find('option:selected').map(function () {
            let role = new Role();
            role.id = $(this).val();
            role.authority = $(this).text();
            return role;
        }).toArray();
        new_user.authorities = selected_roles;

        //push user & clear fields
        api.saveUser(new_user).then(r => {
            $('#newuser').find('input').val('');
            $('#user_roles_new_user').find('option').remove();
            updateUsers();
            $('.nav-tabs a[href="#users"]').tab('show');
        });

    });

    //modal_edit info
    $('#modal_edit').on('shown.bs.modal', function (e) {
        let user = JSON.parse(e.relatedTarget.dataset.user);
        user.password = '';
        let authorities = user.authorities;
        $('#modal_edit .form-control').each(function (index, element) {
            element.value = user[element.name];
        });

        for (let i = 0; i < authorities.length; i++) {
            $('#user_roles_update_modal option[value = ' + authorities[i].id + ']').prop('selected', true);
        }

    })

    //modal_delete info
    $('#modal_delete').on('shown.bs.modal', function (e) {
        let user = JSON.parse(e.relatedTarget.dataset.user);
        user.password = '';
        let authorities = user.authorities;
        $('#modal_delete .form-control').each(function (index, element) {
            element.value = user[element.name];
        });
        for (let i = 0; i < authorities.length; i++) {
            $('#user_roles_delete_modal option[value = ' + authorities[i].id + ']').prop('selected', true);
        }
    })

    $('#button_edit_user').click(function () {
        let new_user = new User();

        $('#modal_edit .form-control').each(function (index, element) {
            new_user[element.name] = element.value;
        });
        let userRolesSelect = $('#user_roles_update_modal');
        new_user.authorities = userRolesSelect.find('option:selected').map(function () {
            let role = new Role();
            role.id = $(this).val();
            role.authority = $(this).text();
            return role;
        }).toArray();

        //push user & clear fields
        api.saveUser(new_user).then(r => {
            $('#updateModal').find('input').val('');
            $('#user_roles_update_modal').find('option').remove();
            $("#updateModal").modal('hide');
            updateUsers();
            $('.nav-tabs a[href="#users"]').tab('show');
        });
    });

    $('#button_delete_user').click(function () {
        let new_user = new User();

        $('#modal_delete .form-control').each(function (index, element) {
            new_user[element.name] = element.value;
        });
        let userRolesSelect = $('#user_roles_delete_modal');
        new_user.roles = userRolesSelect.find('option:selected').map(function () {
            let role = new Role();
            role.id = $(this).val();
            role.authority = "ROLE_" + $(this).text();
            return role;
        }).toArray();

        api.deleteUser(new_user).then(r => {
            $('#modal_delete').find('input').val('');
            $('#user_roles_delete_modal').find('option').remove();
            $("#modal_delete").modal('hide');
            updateUsers();
            $('.nav-tabs a[href="#users"]').tab('show');
        });
    });

    updateUsers();
});