package com.zdmoney.credit.chexiao.vo;

import java.math.BigDecimal;
import java.util.Date;

public class UndoManageVo {
	private Long id;

    private String tradeno;

    private String name;

    private String loanstate;

    private Date tradedate;
    
    private BigDecimal amount;//流水金额
    
    private String  trade_type;
    
    private int promise_return_date;
    
	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public int getPromise_return_date() {
		return promise_return_date;
	}

	public void setPromise_return_date(int promise_return_date) {
		this.promise_return_date = promise_return_date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoanstate() {
		return loanstate;
	}

	public void setLoanstate(String loanstate) {
		this.loanstate = loanstate;
	}

	public Date getTradedate() {
		return tradedate;
	}

	public void setTradedate(Date tradedate) {
		this.tradedate = tradedate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
