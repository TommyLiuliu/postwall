import request from '@/utils/request'

export function pageCategory(data) {
    return request({
        url: '/admin/pageCategory',
        method: 'post',
        params: {
            categoryName: data.categoryName,
            isDelete: data.isDelete,
            curPage: data.curPage,
            pageSize: data.pageSize
        }
    })
}

export function insertCategory(data) {
    return request({
        url: '/admin/insertCategory',
        method: 'post',
        data
    })
}

export function updateCategory(data) {
    return request({
        url: '/admin/updateCategory',
        method: 'post',
        data
    })
}

export function updateIsDelete(id, isDelete) {
    return request({
        url: '/admin/updateIsDelete',
        method: 'post',
        params: {
            id: id,
            isDelete: isDelete
        }
    })
}

export function updateState(id, state) {
    return request({
        url: '/admin/updateState',
        method: 'post',
        params: {
            id: id,
            state: state
        }
    })
}
