import request from '@/utils/request'

export function pageTags(data) {
    return request({
        url: '/admin/pageTags',
        method: 'post',
        params: {
            name: data.name,
            curPage: data.curPage,
            pageSize: data.pageSize
        }
    })
}

export function insertTag(name) {
    return request({
        url: '/admin/insertTag',
        method: 'post',
        params: {
            name: name,
        }
    })
}

export function deleteTag(id) {
    return request({
        url: '/admin/deleteTag',
        method: 'get',
        params: {
            id: id,
        }
    })
}
