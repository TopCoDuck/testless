local limit = redis.call('HGET', KEYS[1], 'limit')
local sold = redis.call('HGET', KEYS[1], 'sold')
if limit == false
  then return true
end
if tonumber(sold) < tonumber(limit)
  then redis.call('HINCRBY', KEYS[1], 'sold', 1)
  return true
end
return false