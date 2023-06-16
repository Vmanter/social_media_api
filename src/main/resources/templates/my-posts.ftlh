<#import "blocks/template.ftlh" as t>
<@t.template user>
<h1 style="text-align: center">Мои публикации</h1>
<hr>
<a class="btn btn-dark mt-3 mb-3" style="text-align: center; width: 100%"
   data-toggle="collapse" href="#collapseExample" role="button"
   aria-expanded="true" aria-controls="collapseExample">
    Добавить новый пост
</a>
<div class="collapse mb-3" id="collapseExample" style="">
    <form action="/post/create" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="postName">Название публикации</label>
            <input type="text" required class="form-control"
                   id="postName" name="title">
        </div>
        <div class="form-group">
            <label for="postName">Название публикации</label>
            <textarea id="text" name="text" rows="10" cols="50" required class="form-control"></textarea>
        </div>
        <div class="form-group">
            <div class="custom-file">
                <input required type="file" name="file1" id="file1">
                <label class="custom-file-label" for="file1">Первая фотография к посту</label>
            </div>
        </div>
        <div class="form-group">
            <div class="custom-file">
                <input type="file" name="file2" id="file2">
                <label class="custom-file-label" for="file2">Вторая фотография к посту</label>
            </div>
        </div>
        <div class="form-group">
            <div class="custom-file">
                <input type="file" name="file3" id="file3">
                <label class="custom-file-label" for="file3">Третья фотография к посту</label>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

        <button type="submit" style="width: 100%" class="btn btn-dark">Добавить</button>
    </form>
</div>
<div class="card-columns">
    <#list posts as post>
    <a href="/post/${post.id}" class="post-link">
        <div class="card text-white bg-dark my-3 post">
            <div class="card-top">
                <form action="/post/${post.id}/delete" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-danger m-3">
                        Удалить
                    </button>
                </form>
            </div>
            <img src="/images/${post.previewImageId}"
                 class="card-img-top">
            <div class="card-body">
                <h5 class="card-title">${post.title}</h5>
                <p class="card-text">${post.text} ₽</p>
            </div>
            <div class="card-footer">
                ${post.dateOfCreated}
            </div>
        </div>
    </a>
    <#else>
    <h1 style="text-align: center">Не найдено</h1>
</#list>
</div>
</@t.template>