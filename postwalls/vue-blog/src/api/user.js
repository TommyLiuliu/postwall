import request from "@/utils/request";

export function register(data) {
    return request({
        url: '/register',
        method: 'post',
        data
    })
}

export function getMailVerify(email) {
    return request({
        url: '/getMailVerify',
        method: 'get',
        params: {
            email: email
        }
    })
}

export function login(data) {
    return request({
        url: '/login',
        method: 'post',
        params: {
            username: data.username,
            password: data.password
        }
    })
}

export function userInfo(token) {
    return request({
        url: '/userInfo',
        method: 'post',
        params: {
            token: token,
        }
    })
}

export function updateEmail(email, verify) {
    return request({
        url: '/updateEmail',
        method: 'post',
        params: {
            email: email,
            verify: verify
        }
    })
}

export function modifyPassword(data) {
    return request({
        url: '/modifyPassword',
        method: 'post',
        params: {
            oldPassword: data.oldPassword,
            newPassword: data.newPassword
        }
    })
}

export function updateUserInfo(data) {
    return request({
        url: '/updateUserInfo',
        method: 'post',
        data
    })
}
