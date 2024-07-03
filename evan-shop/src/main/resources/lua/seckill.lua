--1 參數列表
--1.1
local voucherId = ARGV[1]
--1.2
local userId = ARGV[2]

local stockKey = 'seckill:stock:' .. voucherId
local orderKey = 'seckill:order:' .. voucherId


if(tonumber(redis.call('get',stockKey)) <= 0) then 
    return 1
end

--sismember 判斷存不存在
if (redis.call('sismember', orderKey, userId) == 1) then
    return 2
end
--扣庫存
redis.call('incrby', stockKey, -1)
--下單
redis.call('sadd', orderKey, userId)
return 0