import CryptoJS from 'crypto-js'

export function encrypt(data) {
  const initKey = 'blog'
  const keySize = 128

  const fillKey = (key) => {
    const filledKey = Buffer.alloc(keySize / 8)
    const keys = Buffer.from(key)
    if (keys.length < filledKey.length) {
      filledKey.forEach((b, i) => {
        filledKey[i] = keys[i]
      })
    }
    return filledKey
  }

  const aesEncrypt = (data, key) => {
    const cipher = CryptoJS.AES.encrypt(data, key, {
      mode: CryptoJS.mode.ECB,
      padding: CryptoJS.pad.Pkcs7,
      iv: '',
    })
    const base64Cipher = cipher.ciphertext.toString(CryptoJS.enc.Base64)
    const resultCipher = base64Cipher.replace(/\+/g, '-').replace(/\//g, '_')
    return resultCipher
  }
  const key = CryptoJS.enc.Utf8.parse(fillKey(initKey))

  return aesEncrypt(
    JSON.stringify({
      username: data.username,
      password: data.password,
      verify: data.verify,
      captchaKey: data.captchaKey,
    }),
    key
  )
}
