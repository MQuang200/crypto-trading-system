select u.id, u.username, c.id, c.symbol, c.name, coalesce(a.balance, 0)
from USERS u
cross join CURRENCY c
left join ASSETS a
    on u.id = a.users_id and c.id = a.currency_id;