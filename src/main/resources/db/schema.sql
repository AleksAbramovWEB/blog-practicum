create table if not exists posts
(
    id          bigserial primary key,
    image_url   varchar(2048) not null,
    title       varchar(264)  not null,
    content     text          not null,
    count_likes int           not null,
    created_at  timestamp default now()
);

create table if not exists tags
(
    id   bigserial primary key,
    name varchar(100) unique not null
);

create table if not exists post_tags
(
    post_id bigint not null,
    tag_id  bigint not null,
    primary key (post_id, tag_id),
    constraint fk_post_tags_post foreign key (post_id) references posts (id) on delete cascade,
    constraint fk_post_tags_tag foreign key (tag_id) references tags (id) on delete cascade
);

create index if not exists idx_tags_name on tags (name);
create index if not exists idx_post_tags_post_id on post_tags (post_id);
create index if not exists idx_post_tags_tag_id on post_tags (tag_id);

create table if not exists comments
(
    id         BIGSERIAL PRIMARY KEY,
    post_id    BIGINT NOT NULL,
    text       TEXT   NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    constraint fk_comments_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE
);

create index if not exists idx_comments_post_id on comments (post_id);
create index if not exists idx_comments_created_at on comments (created_at);
create index if not exists idx_comments_post_created on comments (post_id, created_at);
