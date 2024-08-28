$(document).ready(function() {
    const host = "http://localhost:8080"

    // Fetch the list of currencies and populate the select element
    function requestCurrencies() {
        $.ajax({
            url: `${host}/currencies`,
            type: "GET",
            dataType: "json",
            success: function (data) {
                console.log(data);
                const tbody = $('.currencies-table tbody');
                tbody.empty();
                $.each(data, function(index, currency) {
                    console.log(currency);
                    const row = $('<tr></tr>');
                    row.append($('<td></td>').text(currency.Code));
                    row.append($('<td></td>').text(currency.FullName));
                    row.append($('<td></td>').text(currency.Sign));
                    tbody.append(row);
                });

                const newRateBaseCurrency = $("#new-rate-base-currency");
                newRateBaseCurrency.empty();

                // populate the base currency select element with the list of currencies
                $.each(data, function (index, currency) {
                    newRateBaseCurrency.append(`<option value="${currency.Code}">${currency.Code}</option>`);
                });

                const newRateTargetCurrency = $("#new-rate-target-currency");
                newRateTargetCurrency.empty();

                // populate the target currency select element with the list of currencies
                $.each(data, function (index, currency) {
                    newRateTargetCurrency.append(`<option value="${currency.Code}">${currency.Code}</option>`);
                });

                const convertBaseCurrency = $("#convert-base-currency");
                convertBaseCurrency.empty();

                // populate the base currency select element with the list of currencies
                $.each(data, function (index, currency) {
                    convertBaseCurrency.append(`<option value="${currency.Code}">${currency.Code}</option>`);
                });

                const convertTargetCurrency = $("#convert-target-currency");
                convertTargetCurrency.empty();

                // populate the base currency select element with the list of currencies
                $.each(data, function (index, currency) {
                    convertTargetCurrency.append(`<option value="${currency.Code}">${currency.Code}</option>`);
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');

                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    }

    requestCurrencies();

    $("#add-currency").submit(function(e) {
        e.preventDefault();

        $.ajax({
            url: `${host}/currencies`,
            type: "POST",
            data: $("#add-currency").serialize(),
            success: function(data) {
                requestCurrencies();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');

                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });

        return false;
    });

    function requestExchangeRates() {
        $.ajax({
            url: `${host}/exchangeRates`,
            type: "GET",
            dataType: "json",
            success: function(response) {
                console.log(response); // Проверьте, какие данные приходят
                const tbody = $('.exchange-rates-table tbody');
                tbody.empty();
                $.each(response, function(index, rate) {
                    const row = $('<tr></tr>');
                    const currencyPair = rate.BaseCurrencyCode + rate.TargetCurrencyCode;
                    const exchangeRate = rate.Rate;
                    row.append($('<td></td>').text(currencyPair));
                    row.append($('<td></td>').text(exchangeRate));
                    row.append($('<td></td>').html(
                        '<button class="btn btn-secondary btn-sm exchange-rate-edit"' +
                        'data-bs-toggle="modal" data-bs-target="#edit-exchange-rate-modal">Edit</button>'
                    ));
                    tbody.append(row);
                });
            },
            error: function(jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');

                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    }


    requestExchangeRates();

    $(document).delegate('.exchange-rate-edit', 'click', function() {
        // Get the currency and exchange rate from the row
        const pair = $(this).closest('tr').find('td:first').text();
        const exchangeRate = $(this).closest('tr').find('td:eq(1)').text();

        // insert values into the modal
        $('#edit-exchange-rate-modal .modal-title').text(`Edit ${pair} Exchange Rate`);
        $('#edit-exchange-rate-modal #exchange-rate-input').val(exchangeRate);
    });

    // add event handler for edit exchange rate modal "Save" button
    $('#edit-exchange-rate-modal .btn-primary').click(function() {
        // get the currency pair and exchange rate from the modal
        const pair = $('#edit-exchange-rate-modal .modal-title').text().replace('Edit ', '').replace(' Exchange Rate', '');
        const exchangeRate = $('#edit-exchange-rate-modal #exchange-rate-input').val();

        // set changed values to the table row
        const row = $(`tr:contains(${pair})`);
        row.find('td:eq(1)').text(exchangeRate);

        // send values to the server with a patch request
        $.ajax({
            url: `${host}/exchangeRate/${pair}`,
            type: "PATCH",
            contentType : "application/x-www-form-urlencoded",
            data: `rate=${exchangeRate}`,
            success: function() {

            },
            error: function(jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');

                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });

        // close the modal
        $('#edit-exchange-rate-modal').modal('hide');
    });


    $("#add-exchange-rate").submit(function(e) {
        e.preventDefault();

        // Отправка данных на сервер
        $.ajax({
            url: `${host}/exchangeRates`,
            type: "POST",
            data: $("#add-exchange-rate").serialize(), // Данные формы
            success: function(data) {
                requestExchangeRates();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');

                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });

        return false;
    });

    $("#convert").submit(function(e) {
        e.preventDefault();

        const baseCurrency = $("#convert-base-currency").val();
        const targetCurrency = $("#convert-target-currency").val();
        const amount = $("#convert-amount").val();

        $.ajax({
            url: `${host}/exchange?from=${baseCurrency}&to=${targetCurrency}&amount=${amount}`,
            type: "GET",
            // data: "$("#add-exchange-rate").serialize()",
            success: function(data) {
                console.log(data);  // Log the entire response
                console.log(data.ConvertedAmount);  // Check if this is the correct property name

                const convertedAmountInput = $("#convert-converted-amount");
                console.log(convertedAmountInput); // Log the element to ensure it's selected
                convertedAmountInput.val(data.ConvertedAmount);
            },

            error: function(jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');

                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });

        return false;
    });
});