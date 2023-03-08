import request from '@/utils/request'

export function pageUsers(data) {
    return request({
        url: '/admin/pageUsers',
        method: 'get',
        params: {
            username: data.username,
            phone: data.phone,
            email: data.email,
            isDelete: data.isDelete,
            curPage: data.curPage,
            pageSize: data.pageSize
        }
    })
}

export function updateUserIsDelete(userId, isDelete) {
    return request({
        url: '/admin/updateUserIsDelete',
        method: 'get',
        params: {
            userId: userId,
            isDelete: isDelete
        }
    })
}
