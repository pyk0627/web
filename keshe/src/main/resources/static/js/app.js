(function () {
    'use strict';

    // 删除等危险操作二次确认
    document.querySelectorAll('form[data-confirm]').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            var message = form.getAttribute('data-confirm') || '确定执行该操作吗？';
            if (!window.confirm(message)) {
                event.preventDefault();
                event.stopPropagation();
            }
        });
    });

    // 注册页：两次密码一致性
    var registerForm = document.getElementById('registerForm');
    if (registerForm) {
        var password = document.getElementById('password');
        var confirmPassword = document.getElementById('confirmPassword');
        if (password && confirmPassword) {
            var checkPassword = function () {
                if (confirmPassword.value && password.value !== confirmPassword.value) {
                    confirmPassword.setCustomValidity('两次输入的密码不一致');
                } else {
                    confirmPassword.setCustomValidity('');
                }
            };
            password.addEventListener('input', checkPassword);
            confirmPassword.addEventListener('input', checkPassword);
        }
    }

    // 查询页：含油率最低值不能大于最高值
    var searchForm = document.getElementById('searchForm');
    if (searchForm) {
        var oilMin = searchForm.querySelector('input[name="oilMin"]');
        var oilMax = searchForm.querySelector('input[name="oilMax"]');
        if (oilMin && oilMax) {
            var checkOilRange = function () {
                if (oilMin.value && oilMax.value && Number(oilMin.value) > Number(oilMax.value)) {
                    oilMax.setCustomValidity('最高含油率不能小于最低含油率');
                } else {
                    oilMax.setCustomValidity('');
                }
            };
            oilMin.addEventListener('input', checkOilRange);
            oilMax.addEventListener('input', checkOilRange);
        }
    }

    // 结果字段全选 / 清空
    var selectAll = document.getElementById('selectAllFields');
    var clearAll = document.getElementById('clearAllFields');
    if (selectAll) {
        selectAll.addEventListener('click', function () {
            document.querySelectorAll('.field-checkbox').forEach(function (box) {
                box.checked = true;
            });
        });
    }
    if (clearAll) {
        clearAll.addEventListener('click', function () {
            document.querySelectorAll('.field-checkbox').forEach(function (box) {
                box.checked = false;
            });
        });
    }

    // 登录页演示账号一键填充
    document.querySelectorAll('.demo-account').forEach(function (button) {
        button.addEventListener('click', function () {
            var form = document.querySelector('form[action$="/login"]');
            if (!form) {
                return;
            }
            var username = form.querySelector('input[name="username"]');
            var password = form.querySelector('input[name="password"]');
            if (username && password) {
                username.value = button.getAttribute('data-username') || '';
                password.value = button.getAttribute('data-password') || '';
                username.focus();
            }
        });
    });

    // 图片地址实时预览
    document.querySelectorAll('[data-image-input]').forEach(function (input) {
        input.addEventListener('input', function () {
            var form = input.closest('form');
            var preview = form ? form.querySelector('[data-image-preview]') : null;
            if (preview) {
                preview.src = input.value.trim() || '/images/crops/placeholder.svg';
            }
        });
    });

    // Bootstrap 客户端表单验证
    document.querySelectorAll('.needs-validation').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
})();
